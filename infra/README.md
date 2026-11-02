# Clúster RabbitMQ (docker compose)

Dos nodos (`rabbit1`, `rabbit2`) en una EC2 dedicada (por ejemplo t3.small, misma VPC que la EC2 de los microservicios).

## Puesta en marcha

```bash
cd infra
cp .env.example .env        # editar usuario, password y cookie
docker compose up -d
./setup.sh                  # muestra cluster_status y aplica las politicas de retencion
```

Para verificar manualmente:

```bash
docker compose ps
docker compose exec rabbit1 rabbitmqctl cluster_status
docker compose exec rabbit2 rabbitmqctl cluster_status
```

## Que hace cada servicio

| Servicio | Funcion |
|---|---|
| `rabbit1` | Nodo 1 del clúster. AMQP `5672`, Management UI `15672` |
| `rabbit2` | Nodo 2; se une a `rabbit@rabbit1` (peer discovery `classic_config`). AMQP `5673`, UI `15673` |

Ambos usan la **misma cookie Erlang** (requisito para formar el clúster), tienen volumen propio y
`healthcheck` (`rabbit2` espera a que `rabbit1` este sano).

## Security group de la EC2 del clúster

| Puerto | Origen |
|---|---|
| 5672 (AMQP) | security group de la EC2 de los microservicios |
| 8090 (ms-rabbit-admin) | security group de la EC2 del BFF |
| 15672 / 15673 (Management UI) | solo tu IP (no abrir a `0.0.0.0/0`) |
| 22 (SSH) | solo tu IP |

El trafico entre los dos nodos viaja dentro de la red de docker del mismo host, no necesita reglas.

## Conexion de los microservicios

En el `.env` de `ms-auth`, `ms-reservation` y `ms-rabbit-admin`: `RABBITMQ_HOST` = IP **privada** de esta EC2
(se mantiene al apagar y prender el lab), `RABBITMQ_PORT=5672`, y el usuario/clave del `.env` de este directorio.

## Politicas de retencion y limpieza (`setup.sh`)

| Politica | Aplica a | Efecto |
|---|---|---|
| `retencion-colas` | `q.notificaciones`, `q.tickets`, `q.documentos` | TTL 1 h, maximo 10.000 mensajes; lo que expira o desborda va a la DLQ |
| `retencion-dlq` | `q.*.dlq` | TTL 7 dias, maximo 50.000 mensajes (limpieza automatica de la DLQ) |

Las politicas no cambian los argumentos con que se declaran las colas, asi que no provocan `PRECONDITION_FAILED`.

> Si los nodos arrancan con la EC2 recien encendida y `rabbit2` no figura en `running_nodes`:
> `docker compose restart rabbit2` y repetir `cluster_status`.
