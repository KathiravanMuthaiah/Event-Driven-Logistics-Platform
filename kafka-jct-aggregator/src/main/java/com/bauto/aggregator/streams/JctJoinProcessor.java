package com.bauto.aggregator.streams;

import com.bauto.aggregator.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;

public class JctJoinProcessor {

    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        ObjectMapper mapper = new ObjectMapper();

        KStream<String, JitDemand> jitStream = builder.stream("jit-topic");
        KStream<String, JicDemand> jicStream = builder.stream("jic-topic");

        // Logic: join on part_number + location + lead_time-date within time window
        // Evict JIT if no match after 1 hour, forward as standalone calloff

        // Placeholder logic to be filled
        // You will use `join` with `TimeWindows` + eviction logic

        return builder.build();
    }
}
