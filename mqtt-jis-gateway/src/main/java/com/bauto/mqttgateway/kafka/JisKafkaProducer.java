package com.bauto.mqttgateway.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.bauto.mqttgateway.model.JisDemand;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JisKafkaProducer {

    private static final Logger LOG = Logger.getLogger(JisKafkaProducer.class);

    @Inject
    @Channel("jis-out")
    Emitter<String> emitter;
    public void sendToKafka(JisDemand jisDemand) {
        LOG.info("Sending JisDemand to Kafka: " + jisDemand);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(jisDemand);
            LOG.info("Sending jis-topic payload:" + jsonPayload);
            emitter.send(jsonPayload);
        } catch (Exception e) {
            LOG.error("Failed to serialize jisDemand to JSON", e);
        }

    }
}
