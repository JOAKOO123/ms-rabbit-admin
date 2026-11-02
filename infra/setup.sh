#!/usr/bin/env bash
# Verifica el clúster y aplica las políticas de retención y limpieza. Ejecutar después de "docker compose up -d".
set -euo pipefail
cd "$(dirname "$0")"

echo "== Estado del clúster (deben aparecer rabbit@rabbit1 y rabbit@rabbit2 en running_nodes) =="
docker compose exec -T rabbit1 rabbitmqctl cluster_status

echo
echo "== Política de retención de las colas principales (q.xxx) =="
# TTL 1 h y máximo 10.000 mensajes. Con x-dead-letter-exchange en la cola, los mensajes que expiran
# o se descartan por desborde pasan a la DLQ en vez de perderse. ha-mode=all replica la cola en ambos nodos.
docker compose exec -T rabbit1 rabbitmqctl set_policy retencion-colas '^q\.[^.]+$' \
  '{"message-ttl":3600000,"max-length":10000,"overflow":"drop-head","ha-mode":"all","ha-sync-mode":"automatic"}' \
  --priority 10 --apply-to queues

echo "== Política de limpieza de las DLQ (q.xxx.dlq): 7 días y máximo 50.000 mensajes =="
docker compose exec -T rabbit1 rabbitmqctl set_policy retencion-dlq '^q\..+\.dlq$' \
  '{"message-ttl":604800000,"max-length":50000,"overflow":"drop-head","ha-mode":"all","ha-sync-mode":"automatic"}' \
  --priority 20 --apply-to queues

echo
docker compose exec -T rabbit1 rabbitmqctl list_policies
