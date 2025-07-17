package com.bauto.demand.producer;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.bauto.demand.model.JicDemand;
import com.bauto.demand.model.ModelDataSupply;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            LOG.info("Sending MQTT payload:" + jsonPayload);
            emitter.send(jsonPayload);
        } catch (Exception e) {
            LOG.error("Failed to serialize JicProducer to JSON", e);
        }
    }
}
