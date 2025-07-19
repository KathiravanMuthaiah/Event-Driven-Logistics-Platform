package com.bauto.mqttgateway.mqtt;

import java.util.concurrent.Flow.Publisher;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import com.bauto.mqttgateway.kafka.JisKafkaProducer;
import com.bauto.mqttgateway.model.JisDemand;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class JISSubscriber {

    private static final Logger LOG = Logger.getLogger(JISSubscriber.class);

    @Inject
    JisKafkaProducer kafkaProducer;

    @Inject
    @Channel("jis-demand")
    Publisher<String> jisMessages;
    void onStart(@Observes StartupEvent ev) {
        Multi.createFrom().publisher(jisMessages)
            .subscribe().with(this::consumeMQTTJisMessage, failure -> {
                LOG.error("Failed to subscribe to MQTT stream", failure);
            });
    }
    
    public void consumeMQTTJisMessage(String payload) {
        try {
            LOG.info("Received MQTT message: " + payload);
            ObjectMapper mapper = new ObjectMapper();
            JisDemand jisDemand = mapper.readValue(payload, JisDemand.class);
            kafkaProducer.sendToKafka(jisDemand);
        } catch (Exception e) {
            LOG.error("failed to receive and parse the message", e);
        }
    }

}
