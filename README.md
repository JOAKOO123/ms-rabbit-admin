# ms-rabbit-admin

Microservicio administrador de RabbitMQ para el sistema Hotel Boutique (DSY1107).
Crea y elimina colas, exchanges y bindings de la topologia del sistema.

## Requisitos

- Java 21
- Maven 3.9+
- RabbitMQ con la consola de administracion (`rabbitmq:3-management`)

## Seguridad

Todos los endpoints exigen un JWT de Cognito con el grupo `ADMIN`:

- Sin token: `401 Unauthorized`
- Token sin el grupo `ADMIN`: `403 Forbidden`

## Endpoints

| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | `/queues` | Crea una cola |
| DELETE | `/queues/{name}` | Elimina una cola |
| POST | `/exchanges` | Crea un exchange |
| DELETE | `/exchanges/{name}` | Elimina un exchange |
| POST | `/bindings` | Crea un binding |
| DELETE | `/bindings` | Elimina un binding |

Los datos invalidos responden `400` con el detalle de cada campo.

## Ejecutar

```powershell
mvn clean compile
mvn spring-boot:run
```

## Pruebas

```powershell
mvn clean test
```
