package com.isc.dms;

import com.isc.dms.cluster.InstanceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan("com.isc.dms")
public class Application {
    public static void main(String[] args) {
        System.setProperty("instanceId", args[0]);
        ApplicationContext context = SpringApplication.run(Application.class);
        InstanceRegistry instanceRegistry = context.getBean(InstanceRegistry.class);

        instanceRegistry.register(args[0], "localhost", 5000L);
        instanceRegistry.register("ID2", "localhost", 5001L);
        instanceRegistry.register("ID3", "localhost", 5002L);
        instanceRegistry.register("ID4", "localhost", 5003L);
    }

    @Bean(name = "instanceId")
    public String instanceId() {
        return System.getProperty("instanceId");
    }
}
