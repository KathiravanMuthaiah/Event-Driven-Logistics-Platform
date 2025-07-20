package com.bauto.demand.producer;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import com.bauto.demand.model.JitDemand;
import com.bauto.demand.model.ModelDataSupply;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JitProducer {
    private static final Logger LOG = Logger.getLogger(JitProducer.class);
    @Inject
    @Channel("jit-demand")
    Emitter<String> emitter;
    public void sendMockDemand() {
        try {
            JitDemand jit = new JitDemand();
            jit.setDemandId(UUID.randomUUID().toString());
            jit.setPartNumber(ModelDataSupply.getPartNumber());
            jit.setLocationCode(ModelDataSupply.GetLocation());
            jit.setLeadTime(ModelDataSupply.GetLeadTime());
            jit.setTad(ModelDataSupply.GetTad());
            jit.setQuantity(ModelDataSupply.GetQuantity());
            jit.setAddNote(ModelDataSupply.GetAddNote());
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(jit);
            // LOG.info("Sending MQTT payload:" + jsonPayload);

            // ✅ Create Kafka metadata with key = partNumber
            OutgoingKafkaRecordMetadata<String> metadata = OutgoingKafkaRecordMetadata.<String>builder()
                    .withKey(jit.getPartNumber()) // Set Kafka message key
                    .build();

            // ✅ Wrap payload + metadata into a Message
            Message<String> message = Message.of(jsonPayload).addMetadata(metadata);

            LOG.info("Sending Kafka payload with PartNumber key [" + jit.getPartNumber() + "]: " + jsonPayload);

            emitter.send(message);

        } catch (Exception e) {
            LOG.error("Failed to serialize JitDemand to JSON", e);
        }

    }
}
