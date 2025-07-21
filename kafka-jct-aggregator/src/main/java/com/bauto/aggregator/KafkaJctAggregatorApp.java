package com.bauto.aggregator;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.bauto.aggregator.model.CallOffEvent;
import com.bauto.aggregator.model.JicDemand;
import com.bauto.aggregator.model.JitDemand;
import com.bauto.model.SerdeFactory;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class KafkaJctAggregatorApp {

    private static final Logger LOG = Logger.getLogger(KafkaJctAggregatorApp.class);

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @ConfigProperty(name = "app.jit.topic", defaultValue = "jit-topic")
    String jitTopic;

    @ConfigProperty(name = "app.jic.topic", defaultValue = "jic-topic")
    String jicTopic;

    @ConfigProperty(name = "app.jct.output.topic", defaultValue = "calloff-jct-topic")
    String calloffTopic;

    public void start(@Observes StartupEvent event) {
        LOG.info("🚀 Starting Kafka JCT Aggregator Streams");

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-jct-aggregator-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);

        StreamsBuilder builder = new StreamsBuilder();

        // SerDes
        var jitSerde = SerdeFactory.jitDemandSerde();
        var jicSerde = SerdeFactory.jicDemandSerde();
        var calloffSerde = SerdeFactory.callOffEventSerde();

        // Create KStream from JIT and JIC topics
        KStream<String, JitDemand> jitStream = builder.stream(jitTopic, Consumed.with(Serdes.String(), jitSerde));
        KStream<String, JicDemand> jicStream = builder.stream(jicTopic, Consumed.with(Serdes.String(), jicSerde));

        // Re-key both streams by (partNumber + "|" + locationCode + "|" + leadTimeDate)
        KStream<String, JitDemand> rekeyedJit = jitStream.selectKey((k, v) ->
                v.getPartNumber() + "|" + v.getLocationCode() + "|" + v.getLeadTime().split("T")[0]);

        KStream<String, JicDemand> rekeyedJic = jicStream.selectKey((k, v) ->
                v.getPartNumber() + "|" + v.getLocationCode() + "|" + v.getLeadTime().split("T")[0]);

        // Join with 1-hour window
        Duration joinWindow = Duration.ofHours(1);
        KStream<String, CallOffEvent> joined = rekeyedJit.join(
                rekeyedJic,
                (jit, jic) -> {
                    CallOffEvent callOff = new CallOffEvent();
                    callOff.setCallOffId("CALL-" + jit.getDemandId() + "-" + jic.getDemandId());
                    callOff.setPartNumber(jit.getPartNumber());
                    callOff.setLocationCode(jit.getLocationCode());
                    callOff.setLeadTime(jit.getLeadTime());
                    callOff.setTad(jit.getTad());
                    callOff.setQuantity(jit.getQuantity() + jic.getQuantity()); // Aggregated
                    callOff.setDemandType("JCT");
                    callOff.setAdditionalNotes("Merged from JIT + JIC");
                    return callOff;
                },
                JoinWindows.ofTimeDifferenceWithNoGrace(joinWindow),
                StreamJoined.with(Serdes.String(), jitSerde, jicSerde)
        );

        // Send to output topic
        joined.to(calloffTopic, Produced.with(Serdes.String(), calloffSerde));

        // Build and start
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.setUncaughtExceptionHandler((thread, e) -> LOG.error("❌ Uncaught stream error", e));
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
