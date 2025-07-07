#!/bin/bash

set -e
trap 'kill $(jobs -p)' SIGINT SIGTERM

BOOTSTRAP_SERVER="kafka-broker:9092"
KAFKA_CONTAINER="kafka-broker"
NETWORK="bankingnet"

echo "📜 All topics:"
#docker exec "$KAFKA_CONTAINER" kafka-topics.sh --bootstrap-server "$BOOTSTRAP_SERVER" --list
docker run --rm -it --network "${NETWORK}" -e KAFKA_JMX_OPTS="" bitnami/kafka:3.4 kafka-topics.sh --bootstrap-server "$BOOTSTRAP_SERVER" --list


echo "🚀 Creating Kafka topics..."

topics=(
  "transaction_events:3"
  "account_changes:2"
  "audit_logs:1"
  "document_uploaded:1"
  "metrics.service_health:1"
  "metrics.db_health:1"
  "metrics.kafka_health:1"
)

for topic in "${topics[@]}"; do
  IFS=':' read -r name partitions <<< "$topic"
  docker run --rm -it --network "${NETWORK}" -e KAFKA_JMX_OPTS="" bitnami/kafka:3.4 kafka-topics.sh --create --if-not-exists --bootstrap-server "$BOOTSTRAP_SERVER" \
    --replication-factor 1 --partitions "$partitions" --topic "$name"
done

echo "✅ Kafka topics initialized."

wait -n

