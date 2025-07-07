
### 🎯 Objectives for Kafka Setup
| Area               | Requirement                                            |
| ------------------ | ------------------------------------------------------ |
| **Deployment**     | Local via Docker Compose                               |
| **Observability**  | Integrated with metrics/health/tracing                 |
| **Modularity**     | Kafka lives in its own subfolder, shares Docker bridge |
| **Volume Mapping** | Persistent message logs in `./docker-volume/kafka/`    |
| **Monitoring**     | Enable JMX Exporter + expose Prometheus metrics        |
| **Security**       | Open to future mTLS / JWT enhancements                 |

## 📂 Folder Layout

```tree output
infrastructure/
└── kafka/
    ├── docker-compose.yml
    ├── .env.sample
    ├── init/
    │   └── init-topics.sh
    └── jmx-exporter/
        └── kafka-2_0_0.yml


```
## 📦 Components
| Service        | Image                             | Role                  |
| -------------- | --------------------------------- | --------------------- |
| Kafka Broker   | `bitnami/kafka:latest`            | Core message broker   |
| Zookeeper      | `bitnami/zookeeper:latest`        | Coordination service  |
| Prometheus JMX | Built into Kafka via `KAFKA_OPTS` | Exposes Kafka metrics |

## 🔍 Observability Focus
We'll expose JMX metrics from Kafka using a config file to Prometheus

Traceability via trace_id will be designed at producer level

Future plan includes pushing Kafka metrics to Grafana dashboards

