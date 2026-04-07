#!/usr/bin/env bash
# =============================================================
# run_seeds.sh – Apply schema and seed data to MySQL
#
# Usage:
#   ./database/seeds/run_seeds.sh [options]
#
# Options (override via environment variables or flags):
#   DB_HOST     MySQL host           (default: localhost)
#   DB_PORT     MySQL port           (default: 3306)
#   DB_USER     MySQL user           (default: root)
#   DB_PASS     MySQL password       (default: "")
#   --schema-only   Run schema.sql only (skip seeds)
#   --seeds-only    Run seed files only (skip schema)
#
# Examples:
#   DB_USER=admin DB_PASS=secret ./database/seeds/run_seeds.sh
#   ./database/seeds/run_seeds.sh --seeds-only
# =============================================================

set -euo pipefail

# --------------- Configuration --------------------------------
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-root}"
DB_PASS="${DB_PASS:-}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DB_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

RUN_SCHEMA=true
RUN_SEEDS=true

# --------------- Argument parsing -----------------------------
for arg in "$@"; do
  case "${arg}" in
    --schema-only) RUN_SEEDS=false  ;;
    --seeds-only)  RUN_SCHEMA=false ;;
    *)
      echo "Unknown argument: ${arg}" >&2
      exit 1
      ;;
  esac
done

# --------------- Helpers --------------------------------------
mysql_cmd() {
  if [[ -n "${DB_PASS}" ]]; then
    mysql -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" "-p${DB_PASS}" "$@"
  else
    mysql -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" "$@"
  fi
}

run_sql_file() {
  local file="$1"
  echo "  → Running $(basename "${file}") ..."
  mysql_cmd < "${file}"
}

# --------------- Main -----------------------------------------
echo "============================================="
echo " semgroup_app_db – Database Seeder"
echo " Host : ${DB_HOST}:${DB_PORT}"
echo " User : ${DB_USER}"
echo "============================================="

if "${RUN_SCHEMA}"; then
  echo ""
  echo "[1/2] Applying schema ..."
  run_sql_file "${DB_DIR}/schema.sql"
  echo "      Schema applied."
fi

if "${RUN_SEEDS}"; then
  echo ""
  echo "[2/2] Running seed files ..."
  for seed_file in "${SCRIPT_DIR}"/[0-9]*.sql; do
    run_sql_file "${seed_file}"
  done
  echo "      Seeds applied."
fi

echo ""
echo "Done."
