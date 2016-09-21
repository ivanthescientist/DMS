package com.isc.dms.replication;

import com.isc.dms.cluster.InstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ReplicationMessageBus {

    private final Logger logger = LoggerFactory.getLogger(ReplicationMessageBus.class);

    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    @Qualifier("instanceId")
    private String instanceId;

    public void broadcast(ReplicationMessage replicationMessage) {
        instanceRegistry.getInstances().stream()
                .filter(instanceDescriptor -> !instanceDescriptor.getId().equals(instanceId))
                .forEach(instanceDescriptor -> {
                    logger.info("Replicating message to server " + instanceDescriptor.getId());
                });
    }
}
