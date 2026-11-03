#!/usr/bin/env bash
# Verifica el clúster y aplica las políticas. Ejecutar después de "docker compose up -d".
set -euo pipefail
cd "$(dirname "$0")"

echo "== Estado del clúster (deben aparecer rabbit@rabbit1 y rabbit@rabbit2 en running_nodes) =="
docker compose exec -T rabbit1 rabbitmqctl cluster_status

echo
echo "== Limpiando política antigua (si existe) =="
docker compose exec -T rabbit1 rabbitmqctl clear_policy retencion-colas || true

echo
echo "== Política de colas principales (q.xxx): solo alta disponibilidad =="
# TTL y max-length vienen como argumentos de cada cola (declarados por los 4 servicios).
docker compose exec -T rabbit1 rabbitmqctl set_policy colas-principales '^q\.[^.]+$' \
  '{"ha-mode":"all","ha-sync-mode":"automatic"}' \
  --priority 10 --apply-to queues

echo
echo "== Política de retención de DLQ (q.xxx.dlq): 7 días, máx. 50.000, drop-head =="
docker compose exec -T rabbit1 rabbitmqctl set_policy retencion-dlq '^q\..+\.dlq$' \
  '{"message-ttl":604800000,"max-length":50000,"overflow":"drop-head","ha-mode":"all","ha-sync-mode":"automatic"}' \
  --priority 20 --apply-to queues

echo
echo "== Políticas aplicadas =="
docker compose exec -T rabbit1 rabbitmqctl list_policies
echo
echo "== Colas =="
docker compose exec -T rabbit1 rabbitmqctl list_queues name policy messages consumers