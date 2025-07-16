package com.bauto.postgreswriter.deserializer;

import java.nio.charset.StandardCharsets;

import org.jboss.logging.Logger;

import com.bauto.postgreswriter.model.SensorData;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SensorDataDeserializer extends ObjectMapperDeserializer<SensorData> {

    private static final Logger LOG = Logger.getLogger(SensorDataDeserializer.class);

    public SensorDataDeserializer() {
        super(SensorData.class);
        LOG.info("✅ SensorDataDeserializer initialized");
    }

    @Override
    public SensorData deserialize(String topic, byte[] data) {

        try {
            String json = new String(data, StandardCharsets.UTF_8);
            System.out.println("[Deserializer] Raw JSON = " + json);
            SensorData sensor = super.deserialize(topic, data);
            System.out.println("[Deserializer] ✅ Successfully converted: " + sensor);
            return sensor;
        } catch (Exception e) {
            System.err.println("[Deserializer] ❌ Failed to deserialize: " + new String(data));
            e.printStackTrace();
            throw new RuntimeException("Deserialization failed", e);
        }

        // try {
        //     return super.deserialize(topic, data);
        // } catch (Exception e) {
        //     System.err.println("[ERROR] Failed to deserialize SensorData: " + new String(data));
        //     LOG.error(e);
        //     ; // or use SLF4J logger
        //     throw new RuntimeException("Failed to deserialize the Sensor Data", e); // or throw RuntimeException to stop
        //                                                                             // the consumer
        // }
    }
}
