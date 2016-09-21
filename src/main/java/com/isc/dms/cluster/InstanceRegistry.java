package com.isc.dms.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InstanceRegistry {

    @Autowired
    @Qualifier("instanceId")
    private String instanceId;
    private Map<String, InstanceDescriptor> instances = new HashMap<>();

    public void register(String id, String address, Long priority) {
        instances.put(id, new InstanceDescriptor(id, address, priority));
    }

    public Collection<InstanceDescriptor> getInstances() {
        return instances.values();
    }

    public Optional<InstanceDescriptor> getById(String id) {
        return Optional.ofNullable(instances.get(id));
    }

    public InstanceDescriptor getCurrentInstance() {
        return instances.get(instanceId);
    }
}
