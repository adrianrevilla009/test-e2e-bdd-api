#!/usr/bin/env bash
# Start the stack, wait for its healthcheck, run Playwright, always tear down.
set -euo pipefail
cd "$(dirname "$0")"
trap 'docker compose down -v >/dev/null 2>&1' EXIT
docker compose up -d --wait
npm ci --silent
npx playwright test
