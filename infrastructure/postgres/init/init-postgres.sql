-- init-postgres.sql
-- Create schema if you want to separate logically
CREATE SCHEMA IF NOT EXISTS logistics;

-- Sensor Data Table
CREATE TABLE IF NOT EXISTS logistics.sensor_data (
    id SERIAL PRIMARY KEY,
    sensor_id VARCHAR(100) NOT NULL,
    part_number VARCHAR(100),
    location VARCHAR(100),
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    timestamp TIMESTAMP NOT NULL
);

-- CallOff Event Table
CREATE TABLE IF NOT EXISTS logistics.calloff_event (
    id SERIAL PRIMARY KEY,
    calloff_id VARCHAR(100) NOT NULL,
    source VARCHAR(100),
    part_number VARCHAR(100),
    supplier_code VARCHAR(100),
    order_quantity INTEGER,
    delivery_date TIMESTAMP,
    timestamp TIMESTAMP NOT NULL
);

-- Optional: Unified Log Table (if you have any)
CREATE TABLE IF NOT EXISTS logistics.unified_log (
    id SERIAL PRIMARY KEY,
    event_type VARCHAR(50),
    payload JSONB,
    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Create the main table to store unified events
-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS logistics;

-- ================================================================
-- 1. Sensor Data Table
-- ================================================================
CREATE TABLE IF NOT EXISTS logistics.sensor_data (
    id SERIAL PRIMARY KEY,
    sensor_id VARCHAR(100) NOT NULL,
    part_number VARCHAR(100),
    location VARCHAR(100),
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    timestamp TIMESTAMP NOT NULL
);

-- Indexes for sensor_data
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_id ON logistics.sensor_data(sensor_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON logistics.sensor_data(timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_location ON logistics.sensor_data(location);

-- ================================================================
-- 2. Call-Off Event Table (field-aligned to insert)
-- ================================================================
CREATE TABLE IF NOT EXISTS logistics.call_off_event (
    id SERIAL PRIMARY KEY,
    call_off_id VARCHAR(100) NOT NULL,
    supplier_id VARCHAR(100),
    part_number VARCHAR(100),
    quantity INTEGER,
    destination_location VARCHAR(100),
    planned_delivery_time TIMESTAMP,
    status VARCHAR(50)
);

-- Indexes for call_off_event
CREATE INDEX IF NOT EXISTS idx_calloff_event_calloff_id ON logistics.call_off_event(call_off_id);
CREATE INDEX IF NOT EXISTS idx_calloff_event_supplier_id ON logistics.call_off_event(supplier_id);
CREATE INDEX IF NOT EXISTS idx_calloff_event_destination_location ON logistics.call_off_event(destination_location);
CREATE INDEX IF NOT EXISTS idx_calloff_event_planned_delivery_time ON logistics.call_off_event(planned_delivery_time);




