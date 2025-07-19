package com.bauto.postgreswriter.consumer;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.bauto.postgreswriter.model.CallOffEvent;
import com.bauto.postgreswriter.model.JicDemand;
import com.bauto.postgreswriter.model.JisDemand;
import com.bauto.postgreswriter.model.JitDemand;
import com.bauto.postgreswriter.model.SensorData;
import com.bauto.postgreswriter.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventConsumer {

    private static final Logger LOG = Logger.getLogger(EventConsumer.class);

    @Inject
    EventRepository repository;

    @Incoming("sensor-in")
    public void consumeSensorEvent(SensorData sensor) {
        System.out.println("💥 Received sensor event: " + sensor);
        LOG.infof("Received sensor event: %s", sensor);
        repository.insertSensorData(sensor);
    }

    @Incoming("calloff-in")
    public void consumeCallOffEvent(CallOffEvent calloff) {
        LOG.infof("Received calloff event: %s", calloff);
        repository.insertCallOffEvent(calloff);
    }

    @Incoming("jit-in")
    public void consumeJIT(String event) {
        LOG.infof("Received JIT event: %s", event);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JitDemand jit = mapper.readValue(event, JitDemand.class);
            repository.insertJitEvent(jit);
        } catch (Exception e) {
            LOG.error("Failed to process jitDemand and send to target topic", e);
        }
    }

    @Incoming("jic-in")
    public void consumeJIC(String event) {
        LOG.infof("Received JIC event: %s", event);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JicDemand jic = mapper.readValue(event, JicDemand.class);
            repository.insertJicEvent(jic);
        } catch (Exception e) {
            LOG.error("Failed to process jicDemand and send to target topic", e);
        }
    }

    @Incoming("jis-in")
    public void consumeJIS(String event) {
        LOG.infof("Received JIS event: %s", event);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JisDemand jis = mapper.readValue(event, JisDemand.class);
            repository.insertJisEvent(jis).subscribe().with(
                unused -> LOG.info("🎯 Insert completed"),
                err -> LOG.error("💥 Error during insert", err));
        } catch (Exception e) {
            LOG.error("Failed to process jisDemand and send to target topic", e);
        }
    }

}
