package com.bauto.demand.producer;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.bauto.demand.model.JitDemand;
import com.bauto.demand.model.ModelDataSupply;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            LOG.info("Sending MQTT payload:" + jsonPayload);
            emitter.send(jsonPayload);
        } catch (Exception e) {
            LOG.error("Failed to serialize JitDemand to JSON", e);
        }

    }
}
