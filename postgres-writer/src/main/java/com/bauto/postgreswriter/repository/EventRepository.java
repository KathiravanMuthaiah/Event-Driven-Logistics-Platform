package com.bauto.postgreswriter.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.bauto.postgreswriter.model.CallOffEvent;
import com.bauto.postgreswriter.model.JicDemand;
import com.bauto.postgreswriter.model.JisDemand;
import com.bauto.postgreswriter.model.JitDemand;
import com.bauto.postgreswriter.model.SensorData;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventRepository {

    private static final Logger LOG = Logger.getLogger(EventRepository.class);

    @Inject
    PgPool client;

    @Inject
    DataSource dataSource;

    public void insertSensorData(SensorData data) {
        String sql = " INSERT INTO logistics.sensor_data (sensor_id, part_number, location, temperature, humidity, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

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
        String sql = "INSERT INTO logistics.call_off_event "
                + " (call_off_id, part_number, location_code, lead_time,"
                + " tad, quantity, demand_type, additional_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getCallOffId());
            stmt.setString(2, event.getPartNumber());
            stmt.setString(3, event.getLocationCode());
            stmt.setString(4, event.getLeadTime());
            stmt.setInt(5, event.getTad());
            stmt.setInt(6, event.getQuantity());
            stmt.setString(7, event.getDemandType());
            stmt.setString(8, event.getAdditionalNotes());

            stmt.executeUpdate();
            LOG.infof("Inserted call-off event: %s", event.getCallOffId());

        } catch (SQLException e) {
            LOG.error("Failed to insert call-off event", e);
        }
    }

    public void insertJitEvent(JitDemand event) {
        String sql = "INSERT INTO logistics.jit_events (part_number, location_code, lead_time, tad, quantity, add_note) "
                +
                "VALUES ($1, $2, $3, $4, $5, $6)";
        Tuple values = Tuple.of(
                event.getPartNumber(),
                event.getLocationCode(),
                event.getLeadTime(),
                event.getTad(),
                event.getQuantity(),
                event.getAddNote());
        client.preparedQuery(sql).execute(values).subscribe().with(
                result -> LOG.info("✅ Inserted JIT Event: " + event),
                error -> LOG.error("❌ Failed to insert JIT Event", error));
    }

    public void insertJicEvent(JicDemand event) {
        String sql = "INSERT INTO logistics.jic_events (part_number, location_code, lead_time, quantity, add_note) " +
                "VALUES ($1, $2, $3, $4, $5)";
        Tuple values = Tuple.of(
                event.getPartNumber(),
                event.getLocationCode(),
                event.getLeadTime(),
                event.getQuantity(),
                event.getAddNote());
        client.preparedQuery(sql).execute(values).subscribe().with(
                result -> LOG.info("✅ Inserted JIC Event: " + event),
                error -> LOG.error("❌ Failed to insert JIC Event", error));
    }

    public Uni<Void> insertJisEvent(JisDemand demand) {
        String sql = """
                    INSERT INTO logistics.jis_demand (
                        demand_id,
                        demand_name,
                        demand_type,
                        part_numbers,
                        location_code,
                        lead_time,
                        tad,
                        quantity,
                        line_details,
                        add_note
                    ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
                """;
        Tuple values = Tuple.tuple()
                .addValue(demand.getDemandId())
                .addValue(demand.getDemandName())
                .addValue(demand.getDemandType() != null ? demand.getDemandType() : "JIS")
                .addValue(demand.getPartNumbers() != null
                        ? demand.getPartNumbers().toArray(new String[0])
                        : new String[0])
                .addValue(demand.getLocationCode())
                .addValue(demand.getLeadTime())
                .addValue(demand.getTad())
                .addValue(demand.getQuantity())
                .addValue(demand.getLineDetails() != null ? demand.getLineDetails() : "")
                .addValue(demand.getAddNote() != null ? demand.getAddNote() : "");
        return client.preparedQuery(sql).execute(values)
                .onItem().invoke(() -> LOG.info("✅ Inserted JIS Event: " + demand))
                .onFailure().invoke(err -> LOG.error("❌ Failed to insert JIS Event", err))
                .replaceWithVoid();
    }

}
