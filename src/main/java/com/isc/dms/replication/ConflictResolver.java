package com.isc.dms.replication;


import com.isc.dms.cluster.InstanceDescriptor;
import org.springframework.stereotype.Service;

@Service
public class ConflictResolver {

    public String getNewValue(InstanceDescriptor messageOrigin, InstanceDescriptor currentInstance, String oldValue, String newValue) {
        if(messageOrigin.getPriority() > currentInstance.getPriority()) {
            return newValue;
        } else {
            return oldValue;
        }
    }
}
