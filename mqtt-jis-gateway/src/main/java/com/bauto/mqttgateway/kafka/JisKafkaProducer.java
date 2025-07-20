package com.bauto.mqttgateway.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import com.bauto.mqttgateway.model.JisDemand;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
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
            // LOG.info("Sending jis-topic payload:" + jsonPayload);
            // ✅ Create Kafka metadata with key = partNumber
            OutgoingKafkaRecordMetadata<String> metadata = OutgoingKafkaRecordMetadata.<String>builder()
                    .withKey(jisDemand.getLocationCode()) // Set Kafka message key
                    .build();
            
            // ✅ Wrap payload + metadata into a Message
            Message<String> message = Message.of(jsonPayload).addMetadata(metadata);

            LOG.info("Sending Kafka payload with LocationCode key [" + jisDemand.getLocationCode() + "]: " + jsonPayload);

            emitter.send(message);
        } catch (Exception e) {
            LOG.error("Failed to serialize jisDemand to JSON", e);
        }

    }
}
