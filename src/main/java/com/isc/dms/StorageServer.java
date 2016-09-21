package com.isc.dms;

import com.isc.dms.cluster.InstanceRegistry;
import com.isc.dms.replication.ReplicationManager;
import com.isc.dms.replication.ReplicationMessage;
import com.isc.dms.replication.ReplicationMessageBus;
import com.isc.dms.replication.versioning.VersionRegistry;
import com.isc.dms.storage.StorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StorageServer {
    @Autowired
    private ReplicationManager replicationManager;

    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private StorageEngine storageEngine;

    @Autowired
    private VersionRegistry versionRegistry;

    @Autowired
    private ReplicationMessageBus replicationMessageBus;

    @Autowired
    @Qualifier("instanceId")
    private String instanceId;

    public void set(String key, String value) {
        versionRegistry.increment(key, value);
        storageEngine.set(key, value);
        ReplicationMessage message = new ReplicationMessage(instanceId, key, value, versionRegistry.getVersion(key));
        replicationMessageBus.broadcast(message);
    }

    public String get(String key) {
        return storageEngine.get(key);
    }
}
