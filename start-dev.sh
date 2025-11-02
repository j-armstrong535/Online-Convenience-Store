#!/usr/bin/env bash

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cleanup() {
  echo
  echo "Shutting down services..."
  if [[ -n "${BACKEND_PID:-}" ]] && kill -0 "${BACKEND_PID}" 2>/dev/null; then
    echo "Stopping backend (PID ${BACKEND_PID})"
    kill "${BACKEND_PID}" 2>/dev/null || true
  fi
  if [[ -n "${FRONTEND_PID:-}" ]] && kill -0 "${FRONTEND_PID}" 2>/dev/null; then
    echo "Stopping frontend (PID ${FRONTEND_PID})"
    kill "${FRONTEND_PID}" 2>/dev/null || true
  fi
  wait "${BACKEND_PID:-}" "${FRONTEND_PID:-}" 2>/dev/null || true
}

trap cleanup EXIT INT TERM

echo "Starting Spring Boot backend..."
(
  cd "${PROJECT_ROOT}/backend"
  if [[ ! -x "./mvnw" ]]; then
    chmod +x ./mvnw
  fi
  ./mvnw spring-boot:run
) &
BACKEND_PID=$!

echo "Starting React frontend..."
(
  cd "${PROJECT_ROOT}/frontend"
  npm run dev
) &
FRONTEND_PID=$!

wait "${BACKEND_PID}" "${FRONTEND_PID}"
