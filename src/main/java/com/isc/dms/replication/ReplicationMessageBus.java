package com.isc.dms.replication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.dms.cluster.InstanceDescriptor;
import com.isc.dms.cluster.InstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Component
public class ReplicationMessageBus {

    private final Logger logger = LoggerFactory.getLogger(ReplicationMessageBus.class);

    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private ReplicationManager replicationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ZMQ.Context context;

    private Queue<ReplicationMessage> inputMessageQueue = new ConcurrentLinkedQueue<>();
    private Queue<ReplicationMessage> outputMessageQueue = new ConcurrentLinkedQueue<>();

    private Thread inputSocketTask;
    private Thread outputSocketTask;

    public void addMessage(ReplicationMessage replicationMessage) {
        outputMessageQueue.add(replicationMessage);
    }

    @Scheduled(fixedDelay = 100)
    public void processMessages() {
        ReplicationMessage message = inputMessageQueue.poll();
        while (message != null) {
            replicationManager.processMessage(message);
            message = inputMessageQueue.poll();
        }
    }

    public void initialize() {
        Collection<InstanceDescriptor> clusterInstances = instanceRegistry.getClusterInstances();
        InstanceDescriptor currentInstance = instanceRegistry.getCurrentInstance();

        context = ZMQ.context(clusterInstances.size());
        ZMQ.Socket broadcastSocket = context.socket(ZMQ.PUB);
        broadcastSocket.bind("tcp://" + currentInstance.getAddress());

        ZMQ.Socket clientSocket = context.socket(ZMQ.SUB);
        clientSocket.subscribe("".getBytes());

        for(InstanceDescriptor instanceDescriptor : clusterInstances) {
            clientSocket.connect("tcp://" + instanceDescriptor.getAddress());
        }

        inputSocketTask = new Thread(() -> {
            while (!Thread.interrupted()) {
                String serializedReplicationMessage = clientSocket.recvStr();
                if(serializedReplicationMessage != null) {
                    try {
                        ReplicationMessage message = objectMapper.readValue(serializedReplicationMessage, ReplicationMessage.class);
                        inputMessageQueue.add(message);
                    } catch (IOException e) {
                        logger.error("Received malformed replication message: ", e);
                    }
                }
            }
        });

        outputSocketTask = new Thread(() -> {
            while (!Thread.interrupted()) {
                ReplicationMessage replicationMessage = outputMessageQueue.poll();

                while(replicationMessage != null) {
                    try {
                        String serializedMessage = objectMapper.writeValueAsString(replicationMessage);
                        broadcastSocket.send(serializedMessage);
                    } catch (JsonProcessingException e) {
                        logger.error("Unable to serialize message", e);
                    }

                    replicationMessage = outputMessageQueue.poll();
                }
            }
        });

        inputSocketTask.start();
        outputSocketTask.start();
    }

    public void shutdown() {
        inputSocketTask.interrupt();
        outputSocketTask.interrupt();
    }
}
