#!/usr/bin/env sh
set -e

IMAGE_NAME="record/record-me-app:1.0-SNAPSHOT"

docker build -t "${IMAGE_NAME}" -f ./Dockerfile .
echo "Built ${IMAGE_NAME}"
