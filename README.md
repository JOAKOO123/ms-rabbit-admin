# ms-rabbit-admin

Microservicio administrador de RabbitMQ para el sistema Hotel Boutique (DSY1107).
Permite crear y eliminar colas (con su DLQ), exchanges y bindings.

## Requisitos
- Java 21
- Maven 3.9+
- RabbitMQ (local con Docker: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`)

## Variables de entorno
Ver `.env.example`. Las variables se pasan como variables de entorno del sistema o de la IDE, no se suben al repo.

## Puerto
8083

## Endpoints (requieren JWT de Cognito con grupo ADMIN)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /queues | Crea cola (y su DLQ `<nombre>.dlq` si `withDlq=true`) |
| DELETE | /queues/{name} | Elimina cola y su DLQ |
| POST | /exchanges | Crea exchange `direct`, `topic` o `fanout` |
| DELETE | /exchanges/{name} | Elimina exchange |
| POST | /bindings | Crea binding cola-exchange con routing key |
| DELETE | /bindings?queue=&exchange=&routingKey= | Elimina binding |

## Ramas (GitFlow)
- `main`: versiones estables.
- `develop`: integración.
- `feature/*`: nuevas funcionalidades.
- `release/*`: preparación de versiones.
