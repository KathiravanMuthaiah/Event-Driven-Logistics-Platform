package com.bauto.postgreswriter.repository;

import com.bauto.postgreswriter.model.SensorData;
import com.bauto.postgreswriter.model.CallOffEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
public class EventRepository {

    @Inject
    DataSource dataSource;

    public void insertSensorData(SensorData data) {
        String sql = """
            INSERT INTO logistics.sensor_data (
                sensor_id, part_number, location, temperature, humidity, timestamp
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, data.getSensorId());
            stmt.setString(2, data.getPartNumber());
            stmt.setString(3, data.getLocation());
            stmt.setDouble(4, data.getTemperature());
            stmt.setDouble(5, data.getHumidity());
            stmt.setObject(6, data.getTimestamp());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert sensor data", e);
        }
    }

    public void insertCallOffEvent(CallOffEvent event) {
        String sql = """
            INSERT INTO logistics.call_off_event (
                call_off_id, supplier_id, part_number, quantity,
                destination_location, planned_delivery_time, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getCallOffId());
            stmt.setString(2, event.getSupplierId());
            stmt.setString(3, event.getPartNumber());
            stmt.setInt(4, event.getQuantity());
            stmt.setString(5, event.getDestinationLocation());
            stmt.setObject(6, event.getPlannedDeliveryTime());
            stmt.setString(7, event.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert call-off event", e);
        }
    }
}

