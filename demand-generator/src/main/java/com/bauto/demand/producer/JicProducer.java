package com.bauto.demand.producer;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import com.bauto.demand.model.JicDemand;
import com.bauto.demand.model.ModelDataSupply;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JicProducer {
    private static final Logger LOG = Logger.getLogger(JicProducer.class);

    @Inject
    @Channel("jic-demand")
    Emitter<String> emitter;
    public void sendMockDemand() {
        try {
            JicDemand jic = new JicDemand();
            jic.setDemandId(UUID.randomUUID().toString());
            jic.setPartNumber(ModelDataSupply.getPartNumber());
            jic.setLocationCode(ModelDataSupply.GetLocation());
            jic.setLeadTime(ModelDataSupply.GetLeadTime());
            jic.setQuantity(ModelDataSupply.GetQuantity());
            jic.setAddNote(ModelDataSupply.GetAddNote());
            LOG.info("Sending to jic topic message:" + jic);
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(jic);
            // LOG.info("Sending MQTT payload:" + jsonPayload);

            // ✅ Create Kafka metadata with key = partNumber
            OutgoingKafkaRecordMetadata<String> metadata = OutgoingKafkaRecordMetadata.<String>builder()
                    .withKey(jic.getPartNumber()) // Set Kafka message key
                    .build();
            
            // ✅ Wrap payload + metadata into a Message
            Message<String> message = Message.of(jsonPayload).addMetadata(metadata);

            LOG.info("Sending Kafka payload with PartNumber key [" + jic.getPartNumber() + "]: " + jsonPayload);

            emitter.send(message);
        } catch (Exception e) {
            LOG.error("Failed to serialize JicProducer to JSON", e);
        }
    }
}
