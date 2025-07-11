package com.bauto.postgreswriter.mapper;

import com.bauto.postgreswriter.model.SensorData;
import com.bauto.postgreswriter.model.CallOffEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EventMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static SensorData mapSensor(String json) throws IOException {
        return mapper.readValue(json, SensorData.class);
    }

    public static CallOffEvent mapCallOff(String json) throws IOException {
        return mapper.readValue(json, CallOffEvent.class);
    }
}

