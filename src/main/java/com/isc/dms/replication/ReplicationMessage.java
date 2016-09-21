package com.isc.dms.replication;

import com.isc.dms.messaging.Message;
import com.isc.dms.replication.versioning.VectorClock;

public class ReplicationMessage extends Message {
    private String origin;
    private String key;
    private String value;
    private VectorClock clock;

    public ReplicationMessage() {
    }

    public ReplicationMessage(String origin, String key, String value, VectorClock clock) {
        this.origin = origin;
        this.key = key;
        this.value = value;
        this.clock = clock;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public VectorClock getClock() {
        return clock;
    }

    public void setClock(VectorClock clock) {
        this.clock = clock;
    }
}
