package com.isc.dms.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstanceRegistry {
    private String instanceId;

    private Map<String, InstanceDescriptor> instances = new HashMap<>();

    public void register(String id, String address, Long priority) {
        instances.put(id, new InstanceDescriptor(id, address, priority));
    }

    public void register(InstanceDescriptor instanceDescriptor) {
        instances.put(instanceDescriptor.getId(), instanceDescriptor);
    }

    public Collection<InstanceDescriptor> getAllInstances() {
        return instances.values();
    }

    public Collection<InstanceDescriptor> getClusterInstances() {
        return getAllInstances().stream()
                .filter(instanceDescriptor -> !instanceDescriptor.getId().equals(instanceId))
                .collect(Collectors.toSet());
    }

    public Optional<InstanceDescriptor> getById(String id) {
        return Optional.ofNullable(instances.get(id));
    }

    public InstanceDescriptor getCurrentInstance() {
        return instances.get(instanceId);
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
