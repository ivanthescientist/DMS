package com.isc.dms.replication;

import com.isc.dms.cluster.InstanceDescriptor;
import com.isc.dms.cluster.InstanceRegistry;
import com.isc.dms.replication.versioning.Occurrence;
import com.isc.dms.replication.versioning.VectorClock;
import com.isc.dms.replication.versioning.VectorClockComparator;
import com.isc.dms.replication.versioning.VersionRegistry;
import com.isc.dms.storage.StorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplicationManager {

    @Autowired
    private StorageEngine storageEngine;

    @Autowired
    private ConflictResolver conflictResolver;

    @Autowired
    private VersionRegistry versionRegistry;

    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private VectorClockComparator clockComparator;


    public void processMessage(ReplicationMessage replicationMessage) {
        VectorClock currentVersion = versionRegistry.getVersion(replicationMessage.getKey());
        VectorClock newVersion = replicationMessage.getClock();
        Occurrence comparisonResult = clockComparator.compare(currentVersion, newVersion);
        VectorClock mergedClock = currentVersion.merge(newVersion);

        String value = storageEngine.get(replicationMessage.getValue());

        switch (comparisonResult) {
            case AFTER:
                value = replicationMessage.getValue();
                break;
            case BEFORE:
                break;
            default:
                value = resolveConflict(replicationMessage, value);
                break;
        }
        storageEngine.set(replicationMessage.getKey(), value);
        versionRegistry.update(replicationMessage.getKey(), mergedClock);
    }

    private String resolveConflict(ReplicationMessage replicationMessage, String oldValue) {
        InstanceDescriptor currentInstance = instanceRegistry.getCurrentInstance();
        InstanceDescriptor originInstance = instanceRegistry.getById(replicationMessage.getOrigin()).orElseThrow(() -> {
           return new RuntimeException("Unknown instance " + replicationMessage.getOrigin());
        });

        return conflictResolver.getNewValue(originInstance, currentInstance, oldValue, replicationMessage.getValue());
    }
}
