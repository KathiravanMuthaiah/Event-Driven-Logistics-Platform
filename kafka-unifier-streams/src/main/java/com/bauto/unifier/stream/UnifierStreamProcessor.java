package com.bauto.unifier.stream;

import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import com.bauto.unifier.model.CallOffEvent;
import com.bauto.unifier.model.JicDemand;
import com.bauto.unifier.model.JisDemand;
import com.bauto.unifier.model.JitDemand;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UnifierStreamProcessor {

    private static final Logger LOG = Logger.getLogger(UnifierStreamProcessor.class);

    @Incoming("jit-in")
    @Outgoing("unified-out")
    public String unifyJit(String jitString) {
        String jsonPayload = null;
        try {

            ObjectMapper mapper = new ObjectMapper();
            JitDemand jit = mapper.readValue(jitString, JitDemand.class);
            LOG.infof("Unifying JIT: %s", jit.getPartNumber());
            CallOffEvent co = new CallOffEvent();
            co.setCallOffId(UUID.randomUUID().toString());
            co.setPartNumber(jit.getPartNumber());
            co.setLocationCode(jit.getLocationCode());
            co.setLeadTime(jit.getLeadTime());
            co.setQuantity(jit.getQuantity());
            co.setDemandType("JIT");
            co.setAdditionalNotes(jit.getAddNote());
            jsonPayload = mapper.writeValueAsString(co);
            LOG.info("Sending jit-topic payload:" + jsonPayload);
            return jsonPayload;
        } catch (Exception e) {
            LOG.error("Failed to process jitDemand and send to target topic", e);
        }
        return null;
    }

    @Incoming("jic-in")
    @Outgoing("unified-out")
    public String unifyJic(String jicString) {
        String jsonPayload = null;
        try {

            ObjectMapper mapper = new ObjectMapper();
            JicDemand jic = mapper.readValue(jicString, JicDemand.class);
            LOG.infof("Unifying JIC: %s", jic.getPartNumber());
            CallOffEvent co = new CallOffEvent();
            co.setCallOffId(UUID.randomUUID().toString());
            co.setPartNumber(jic.getPartNumber());
            co.setLocationCode(jic.getLocationCode());
            co.setLeadTime(jic.getLeadTime());
            co.setQuantity(jic.getQuantity());
            co.setDemandType("JIC");
            co.setAdditionalNotes(jic.getAddNote());
            jsonPayload = mapper.writeValueAsString(co);
            LOG.info("Sending jic-topic payload:" + jsonPayload);
            return jsonPayload;
        } catch (Exception e) {
            LOG.error("Failed to process jicDemand and send to target topic", e);
        }
        return null;
    }

    @Incoming("jis-in")
    @Outgoing("unified-out")
    public Multi<String> unifyJis(String jisString) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            JisDemand jis = mapper.readValue(jisString, JisDemand.class);
            LOG.infof("Unifying JIS for %d parts", jis.getPartNumbers().size());
            return Multi.createFrom().iterable(
                    jis.getPartNumbers().stream().map(pn -> {
                        String jsonPayload = null;
                        CallOffEvent co = new CallOffEvent();
                        co.setCallOffId(UUID.randomUUID().toString());
                        co.setPartNumber(pn);
                        co.setLocationCode(jis.getLocationCode());
                        co.setLeadTime(jis.getLeadTime());
                        co.setQuantity(jis.getQuantity());
                        co.setDemandType("JIS");
                        co.setAdditionalNotes(jis.getLineDetails() + " | " + jis.getAddNote());
                        try {
                            jsonPayload = mapper.writeValueAsString(co);
                            LOG.info("Sending jis-topic payload:" + jsonPayload);
                            return jsonPayload;
                        } catch (Exception e) {
                            LOG.error("Failed to process jisDemand and send to target topic for PartNumber" + pn, e);
                        }
                        return null;
                    }).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("Failed to process jisDemand and send to target topic", e);
        }
        return null;

    }
}
