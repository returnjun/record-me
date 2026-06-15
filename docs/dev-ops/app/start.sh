#!/usr/bin/env sh
set -e

CONTAINER_NAME=record-me-app
IMAGE_NAME=record/record-me-app:1.0-SNAPSHOT
PORT=8088

echo "Starting ${CONTAINER_NAME}"

docker stop "${CONTAINER_NAME}" 2>/dev/null || true
docker rm "${CONTAINER_NAME}" 2>/dev/null || true

docker run --name "${CONTAINER_NAME}" \
  --network record-me-network \
  -e TZ=Asia/Shanghai \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SERVER_PORT="${PORT}" \
  -p "${PORT}:${PORT}" \
  -v "$(pwd)/../log:/data/log" \
  -d "${IMAGE_NAME}"

echo "Started ${CONTAINER_NAME}"
docker logs -f "${CONTAINER_NAME}"
