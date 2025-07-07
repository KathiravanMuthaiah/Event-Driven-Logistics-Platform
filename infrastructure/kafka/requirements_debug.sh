#!/bin/sh

# requirements_debug.sh
# Install basic debug tools — for dev/test only

set -e

echo "[INFO] Installing debug tools..."
apt-get update && \
apt-get install -y --no-install-recommends \
    curl \
    net-tools \
    iputils-ping \
    dnsutils \
    iproute2 \
    lsof \
    procps \
    less \
    vim \
    htop \
    nano && \
apt-get clean && rm -rf /var/lib/apt/lists/*

echo "[INFO] Debug tools installed."

