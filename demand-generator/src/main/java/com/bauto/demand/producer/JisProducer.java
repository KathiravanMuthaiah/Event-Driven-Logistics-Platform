package com.bauto.demand.producer;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.bauto.demand.model.JisDemand;
import com.bauto.demand.model.ModelDataSupply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JisProducer {

    private static final Logger LOG = Logger.getLogger(JisProducer.class);

    @Inject
    @Channel("jis-demand")
    Emitter<String> emitter;

    public void sendMockMqttDemand() {
        try {
            JisDemand jis = new JisDemand();
            jis.setDemandId(UUID.randomUUID().toString());
            jis.setPartNumbers(Arrays.asList(new String[] { ModelDataSupply.getPartNumber(),
                    ModelDataSupply.getPartNumber(), ModelDataSupply.getPartNumber() }));
            jis.setLocationCode(ModelDataSupply.GetLocation());
            jis.setLeadTime(ModelDataSupply.GetLeadTime());
            jis.setTad(ModelDataSupply.GetTad());
            jis.setQuantity(ModelDataSupply.GetQuantity());
            jis.setAddNote(ModelDataSupply.GetAddNote());
            jis.setLineDetails("l1->l7->l4->l2");

            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(jis);
            LOG.info("Sending MQTT payload:" + jsonPayload);
            emitter.send(jsonPayload);
        } catch (Exception e) {
            LOG.error("Failed to serialize JisDemand to JSON", e);
        }

    }
}
