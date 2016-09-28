package com.isc.dms.configuration;

import com.isc.dms.replication.ReplicationMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class ServerInitializer {
    private Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Value("${config.path}")
    private String configPath;

    @Autowired
    private ReplicationMessageBus replicationMessageBus;

    @PostConstruct
    public void initialize() {
        logger.info("Loading configs from: " + configPath);
        configurationLoader.load(configPath);
        replicationMessageBus.initialize();
    }
}
