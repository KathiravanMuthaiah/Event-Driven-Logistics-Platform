package com.bauto.postgreswriter.consumer;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.bauto.postgreswriter.model.CallOffEvent;
import com.bauto.postgreswriter.model.SensorData;
import com.bauto.postgreswriter.repository.EventRepository;

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
}

