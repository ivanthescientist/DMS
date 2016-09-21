package com.isc.dms.cluster;


public class InstanceDescriptor {
    private String id;
    private String address;
    private Long priority;

    public InstanceDescriptor(String id, String address, Long priority) {
        this.id = id;
        this.address = address;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Long getPriority() {
        return priority;
    }
}
