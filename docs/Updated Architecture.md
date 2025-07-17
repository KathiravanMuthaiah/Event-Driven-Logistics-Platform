# Updated Architecture

### 🧠 **Business Logic Flow:**

| Role                             | Description                                                  |
| -------------------------------- | ------------------------------------------------------------ |
| **Demand Generator (1 service)** | One microservice generates all demand types (JIT, JIC, JIS payload → via MQTT) |
| **MQTT Gateway (JIS)**           | Another microservice listens to MQTT → transforms → pushes to `jis-topic` |
| **Kafka Streams Processor**      | Consolidates messages from `jit-topic`, `jic-topic`, `jis-topic` → creates call-off |
| **Call-Off Unifier**             | Produces to `unified-topic` based on aggregation rules (lead_time, part_number, quantity) |
| **Downstream Consumers**         | `supplychain-mock`, `warehouse-mock` read from `unified-topic` for business action |
| **DB Writers**                   | Each topic has a writer that persists messages into respective PostgreSQL tables |



------

## 🔄 Updated Component Breakdown

| Component               | Description                                                  |
| ----------------------- | ------------------------------------------------------------ |
| `demand-generator`      | 🧠 Smart scheduler emitting JIT, JIC, and MQTT-formatted JIS messages |
| `mqtt-jis-gateway`      | 🌐 MQTT Subscriber → Kafka producer for JIS                   |
| `kafka-unifier-streams` | 🔄 Merges & transforms JIT/JIC/JIS into a normalized **call-off event** |
| `postgres-writer`       | 🗂️ Subscribes to all raw and unified topics, persists to DB   |
| `supplychain-mock`      | 🏭 Reacts to `unified-topic` to simulate supply triggers      |
| `warehouse-mock`        | 🏬 Simulates stock management and movement                    |



------

## 🎯 Benefits of This Design

| Advantage                       | Description                                                  |
| ------------------------------- | ------------------------------------------------------------ |
| ✅ **Unified Demand Simulation** | One generator allows easier config for timing, part types, etc. |
| ✅ **IoT Realism**               | MQTT simulates physical line/vehicle demands (JIS), kept decoupled |
| ✅ **Stream-Driven Aggregation** | Kafka Streams takes responsibility for consolidation logic   |
| ✅ **Traceability**              | Per-topic DB tables allow audit/log review                   |
| ✅ **Extensible**                | Easy to add anomaly injection, failure testing, delay scenarios |

## ✅ 1. JIT (Just-In-Time) Message

**📦 Purpose:** Time-critical parts for production, delivered exactly when needed.

```json
{
  "part_number": "ABC-123",
  "location_code": "LINE-01",
  "lead_time": 2,
  "tad": "2025-07-17T10:00:00Z",
  "quantity": 15,
  "add_note": "Critical engine component"
}
```

| Field           | Type    | Description                             |
| --------------- | ------- | --------------------------------------- |
| `part_number`   | String  | Identifier of the part                  |
| `location_code` | String  | Target assembly line or sub-location    |
| `lead_time`     | Integer | In hours or days (configurable unit)    |
| `tad`           | String  | Time of Actual Delivery (ISO timestamp) |
| `quantity`      | Integer | Required quantity                       |
| `add_note`      | String  | Optional note for logistics team        |



📌 **Kafka Topic:** `jit-topic`
 📂 **DB Table:** `jit_events`

------

## ✅ 2. JIC (Just-In-Case) Message

**📦 Purpose:** Long lead-time parts, ordered in bulk for risk mitigation or stock buffer.

```json
{
  "part_number": "DEF-456",
  "location_code": "WH-A1",
  "lead_time": 10,
  "quantity": 100,
  "add_note": "Bulk order for seasonal demand"
}
```

| Field           | Type    | Description                       |
| --------------- | ------- | --------------------------------- |
| `part_number`   | String  | Identifier of the part            |
| `location_code` | String  | Stock or warehouse destination    |
| `lead_time`     | Integer | Longer lead time in hours or days |
| `quantity`      | Integer | Bulk quantity                     |
| `add_note`      | String  | Optional freeform note            |



📌 **Kafka Topic:** `jic-topic`
 📂 **DB Table:** `jic_events`

------

## ✅ 3. JIS (Just-In-Sequence) Message

**📦 Purpose:** Special sequence delivery of grouped parts (e.g., for a vehicle).

```json
{
  "part_numbers": ["XYZ-789", "XYZ-790", "XYZ-791"],
  "location_code": "LINE-02",
  "lead_time": 1,
  "tad": "2025-07-17T08:30:00Z",
  "quantity": 1,
  "line_details": {
    "vehicle_id": "VIN-2025-00123",
    "sequence_number": "SEQ-007"
  },
  "add_note": "Full bumper assembly sequence"
}
```

| Field           | Type          | Description                           |
| --------------- | ------------- | ------------------------------------- |
| `part_numbers`  | Array[String] | Multiple parts grouped per sequence   |
| `location_code` | String        | Target production location            |
| `lead_time`     | Integer       | Time allowed before usage             |
| `tad`           | String        | Exact timestamp of required arrival   |
| `quantity`      | Integer       | Typically 1, grouped per car or batch |
| `line_details`  | Object        | Extra info: vehicle ID, sequence      |
| `add_note`      | String        | Freeform comment                      |



📌 **Kafka Topic:** `jis-topic`
 📂 **DB Table:** `jis_events`

------

## ✅ 4. Unified Call-Off Message

**📦 Purpose:** Final computed instruction for supplier after merging JIT, JIC, and JIS needs.

```json
{
  "call_off_id": "CALL-0001",
  "supplier_id": "SUPP-001",
  "part_number": "ABC-123",
  "quantity": 40,
  "destination_location": "LINE-01",
  "planned_delivery_time": "2025-07-17T10:00:00Z",
  "status": "PENDING"
}
```

| Field                   | Type    | Description                              |
| ----------------------- | ------- | ---------------------------------------- |
| `call_off_id`           | String  | Unique ID for tracking                   |
| `supplier_id`           | String  | Mapped supplier for that part            |
| `part_number`           | String  | Unified from JIT/JIC/JIS                 |
| `quantity`              | Integer | Consolidated quantity                    |
| `destination_location`  | String  | Target location (unified)                |
| `planned_delivery_time` | String  | Chosen/planned time based on priority    |
| `status`                | String  | Initial status: `PENDING` or `CONFIRMED` |



📌 **Kafka Topic:** `unified-topic`
 📂 **DB Table:** `call_off_event`



## ✅ 1. `jit_events` Table

```sql
CREATE TABLE IF NOT EXISTS logistics.jit_events (
    id SERIAL PRIMARY KEY,
    part_number VARCHAR(100) NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time INTEGER NOT NULL,
    tad TIMESTAMP WITH TIME ZONE NOT NULL,
    quantity INTEGER NOT NULL,
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_jit_part_number ON logistics.jit_events(part_number);
CREATE INDEX IF NOT EXISTS idx_jit_tad ON logistics.jit_events(tad);
CREATE INDEX IF NOT EXISTS idx_jit_location ON logistics.jit_events(location_code);
```

------

## ✅ 2. `jic_events` Table

```sql
CREATE TABLE IF NOT EXISTS logistics.jic_events (
    id SERIAL PRIMARY KEY,
    part_number VARCHAR(100) NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_jic_part_number ON logistics.jic_events(part_number);
CREATE INDEX IF NOT EXISTS idx_jic_location ON logistics.jic_events(location_code);
```

------

## ✅ 3. `jis_events` Table

```sql
CREATE TABLE IF NOT EXISTS logistics.jis_events (
    id SERIAL PRIMARY KEY,
    part_numbers TEXT[] NOT NULL,
    location_code VARCHAR(100) NOT NULL,
    lead_time INTEGER NOT NULL,
    tad TIMESTAMP WITH TIME ZONE NOT NULL,
    quantity INTEGER NOT NULL,
    vehicle_id VARCHAR(100),
    sequence_number VARCHAR(100),
    add_note TEXT,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_jis_tad ON logistics.jis_events(tad);
CREATE INDEX IF NOT EXISTS idx_jis_location ON logistics.jis_events(location_code);
CREATE INDEX IF NOT EXISTS idx_jis_vehicle_id ON logistics.jis_events(vehicle_id);
```

> 💡 `part_numbers` is stored as a `TEXT[]` array to accommodate multiple parts per message.

------

## ✅ 4. `call_off_event` Table

```sql
CREATE TABLE IF NOT EXISTS logistics.call_off_event (
    id SERIAL PRIMARY KEY,
    call_off_id VARCHAR(100) NOT NULL,
    supplier_id VARCHAR(100) NOT NULL,
    part_number VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    destination_location VARCHAR(100) NOT NULL,
    planned_delivery_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE UNIQUE INDEX IF NOT EXISTS idx_calloff_id ON logistics.call_off_event(call_off_id);
CREATE INDEX IF NOT EXISTS idx_calloff_supplier ON logistics.call_off_event(supplier_id);
CREATE INDEX IF NOT EXISTS idx_calloff_part_number ON logistics.call_off_event(part_number);
CREATE INDEX IF NOT EXISTS idx_calloff_delivery_time ON logistics.call_off_event(planned_delivery_time);
```

------

## 🧹 Additional Notes

- All tables include a `received_at` or `created_at` timestamp for event tracking.
- Schema: `logistics` is used consistently across all tables.
- Naming convention aligns with Kafka topic names for clarity.



### ✅ Plan for `postgres-writer` Enhancement

#### 🔄 Topics & Event Types to Support

| Kafka Topic     | Event Class    | Target Table               |
| --------------- | -------------- | -------------------------- |
| `jit-topic`     | `JITEvent`     | `logistics.jit_event`      |
| `jic-topic`     | `JICEvent`     | `logistics.jic_event`      |
| `jis-topic`     | `JISEvent`     | `logistics.jis_event`      |
| `unified-topic` | `CallOffEvent` | `logistics.call_off_event` |