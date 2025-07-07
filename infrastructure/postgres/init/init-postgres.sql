-- init-postgres.sql
-- Create schema if you want to separate logically
CREATE SCHEMA IF NOT EXISTS logistics;

-- Create the main table to store unified events
CREATE TABLE IF NOT EXISTS logistics.logistics_events (
    id SERIAL PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    part_number VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    sensor_id VARCHAR(50),
    temperature DOUBLE PRECISION,
    sensor_status VARCHAR(20),
    event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Create index on order_id for faster searches
CREATE INDEX IF NOT EXISTS idx_logistics_order_id
    ON logistics.logistics_events(order_id);

-- Create index on sensor_id for sensor-specific queries
CREATE INDEX IF NOT EXISTS idx_logistics_sensor_id
    ON logistics.logistics_events(sensor_id);

-- Optional: index on event timestamp
CREATE INDEX IF NOT EXISTS idx_logistics_event_timestamp
    ON logistics.logistics_events(event_timestamp);



