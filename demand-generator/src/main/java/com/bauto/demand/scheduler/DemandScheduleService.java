package com.bauto.demand.scheduler;

import com.bauto.demand.producer.JicProducer;
import com.bauto.demand.producer.JisProducer;
import com.bauto.demand.producer.JitProducer;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DemandScheduleService {

    @Inject JitProducer jitProducer;
    @Inject JicProducer jicProducer;
    @Inject JisProducer jisProducer;

    @Scheduled(every = "30s")
    public void generateDemands() {
        jitProducer.sendMockDemand();
        jicProducer.sendMockDemand();
        jisProducer.sendMockMqttDemand();  // Simulates MQTT-originated input
    }
}
