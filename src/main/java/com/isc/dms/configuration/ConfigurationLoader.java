package com.isc.dms.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.dms.cluster.InstanceRegistry;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class ConfigurationLoader {

    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private ObjectMapper mapper;

    public void load(String path) {
        loadFromFile(path)
                .flatMap(this::parseConfiguration)
                .ifPresent(this::populateConfiguration);
    }

    public Optional<String> loadFromFile(String path) {
        try {
            return Optional.of(FileUtils.readFileToString(new File(path)));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public Optional<ServerConfiguration> parseConfiguration(String configuration) {
        try {
            return Optional.of(mapper.readValue(configuration, ServerConfiguration.class));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public void populateConfiguration(ServerConfiguration serverConfiguration) {
        instanceRegistry.setInstanceId(serverConfiguration.getInstanceId());
        serverConfiguration.getInstanceDescriptors()
                .forEach(instanceRegistry::register);
    }
}
