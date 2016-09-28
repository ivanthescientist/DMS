package com.isc.dms.configuration;


import com.isc.dms.cluster.InstanceDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ServerConfiguration {
    private String instanceId;
    private List<InstanceDescriptor> instanceDescriptors = new ArrayList<>();

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public List<InstanceDescriptor> getInstanceDescriptors() {
        return instanceDescriptors;
    }

    public void setInstanceDescriptors(List<InstanceDescriptor> instanceDescriptors) {
        this.instanceDescriptors = instanceDescriptors;
    }
}
