-- init-postgres.sql
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
    part_number VARCHAR(100) NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time VARCHAR(50) NOT NULL,
    tad INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    demand_type VARCHAR(20) NOT NULL,  -- JIT, JIS, JIC
    additional_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);


-- Indexes
-- Unique ID for deduplication
CREATE UNIQUE INDEX IF NOT EXISTS idx_calloff_calloff_id   ON logistics.call_off_event(call_off_id);
CREATE INDEX IF NOT EXISTS idx_calloff_part_number   ON logistics.call_off_event(part_number);
CREATE INDEX IF NOT EXISTS idx_calloff_location_code   ON logistics.call_off_event(location_code);
CREATE INDEX IF NOT EXISTS idx_calloff_lead_time   ON logistics.call_off_event(lead_time);
CREATE INDEX IF NOT EXISTS idx_calloff_demand_type   ON logistics.call_off_event(demand_type);



CREATE TABLE IF NOT EXISTS logistics.jit_events (
    id SERIAL PRIMARY KEY,
    part_number VARCHAR(100) NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time VARCHAR(50) NOT NULL,
    tad INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_jit_part_number ON logistics.jit_events(part_number);
CREATE INDEX IF NOT EXISTS idx_jit_tad ON logistics.jit_events(tad);
CREATE INDEX IF NOT EXISTS idx_jit_location ON logistics.jit_events(location_code);


CREATE TABLE IF NOT EXISTS logistics.jic_events (
    id SERIAL PRIMARY KEY,
    part_number VARCHAR(100) NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_jic_part_number ON logistics.jic_events(part_number);
CREATE INDEX IF NOT EXISTS idx_jic_location ON logistics.jic_events(location_code);


CREATE TABLE IF NOT EXISTS logistics.jis_demand (
    id SERIAL PRIMARY KEY,
    demand_id VARCHAR(100) NOT NULL,
    demand_name VARCHAR(150),
    demand_type VARCHAR(50) NOT NULL,
    part_numbers TEXT[] NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time VARCHAR(50) NOT NULL,
    tad INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    line_details TEXT,
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);


CREATE INDEX IF NOT EXISTS idx_jis_demand_id ON logistics.jis_demand(demand_id);
CREATE INDEX IF NOT EXISTS idx_jis_location_code ON logistics.jis_demand(location_code);
CREATE INDEX IF NOT EXISTS idx_jis_part_numbers_gin ON logistics.jis_demand USING GIN(part_numbers);
CREATE INDEX IF NOT EXISTS idx_jis_received_at ON logistics.jis_demand(received_at);




