package com.bauto.postgreswriter.deserializer;

import org.jboss.logging.Logger;

import com.bauto.postgreswriter.model.CallOffEvent;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CallOffEventDeserializer extends ObjectMapperDeserializer<CallOffEvent> {

     private static final Logger LOG = Logger.getLogger(CallOffEventDeserializer.class);

    public CallOffEventDeserializer() {
        super(CallOffEvent.class);
        LOG.info("✅ CallOffEventDeserializer initialized");
    }

    @Override
    public CallOffEvent deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to deserialize CallOffEvent: " + new String(data));
            LOG.error(e);
            throw new RuntimeException("Failed to deserialize CallOffEvent",e);
        }
    }
}
