package com.bauto.mqttgateway;

import org.jboss.logging.Logger;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MqttGatewayApplication {
    private static final Logger LOG = Logger.getLogger(MqttGatewayApplication.class);

    public static void main(String... args) {
        LOG.info("Application MqttGatewayApplication started");
        Quarkus.run(args);
    }
}
