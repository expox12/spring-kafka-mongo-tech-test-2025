# AVORIS – Prueba Técnica
Entorno Docker Compose listo para ejecutar MongoDB, Kafka y el microservicio `search-service`.

## ✔️ 1. Servicios incluidos

| Servicio            | Contenedor        | Puerto | Descripción |
|--------------------|-------------------|--------|-------------|
| MongoDB            | c-mongo           | 27017  | Base de datos |
| Mongo Express | c-mongo-express   | 8081   | UI para ver datos |
| Kafka  | c-kafka           | 9092/9093 | Mensajería |
| Search Service     | c-search-service  | 8080   | Microservicio Spring Boot |

Los servicios están distribuidos en dos redes Docker:
- `mongo-net`
- `kafka-net`

## ✔️ 2. Arrancar en modo **producción**
Este modo levanta todos los servicios dockerizados listo para ser probado:
- MongoDB
- Mongo Express
- Kafka
- Search Service

```bash
docker compose --profile prod up -d --build
```

## ✔️ 3. Arrancar sin definir perfil
Este modo levanta todos los servicios excepto Search Service.
```bash
docker compose up -d --build
```
Objetivo:
* Ejecución a parte este servicio mediante un IDE como IntelliJ para debuguear el código, pudiendo acceder al resto de servicios.
* Compilación con el wrapper de maven y pruebas unitarias: 
```bash
  ./mvnw clean install -f pom.xml 
```
* Ejecución con maven:
```bash
  ./mvnw spring-boot:run
```
Si se decide dockerizar Search Service:
```bash
docker compose up -d s-search-service
```
## ✔️ 4. Probar el servicio
Una vez todo está arriba, el backend queda disponible en:

👉 http://localhost:8080

### ➤ 4.1 Crear búsqueda
```bash
curl -X POST http://localhost:8080/search \
-H "Content-Type: application/json" \
-d '{
"hotelId": "H100",
"checkIn": "10/11/2025",
"checkOut": "15/11/2025",
"ages": [32, 29]
}'
```
Respuesta esperada:
```json
{
"searchId": "c4b1e788-e353-4ff4-aef7-47a3734da1e0"
}
```

### ➤ 4.2 Consultar búsquedas similares
```bash
curl "http://localhost:8080/count?searchId=ID_DEVUELTO"
```
Respuesta esperada:
```json
{
  "searchId": "c4b1e788-e353-4ff4-aef7-47a3734da1e0",
  "search": {
    "hotelId": "H100",
    "checkIn": "10/11/2025",
    "checkOut": "15/11/2025",
    "ages": [32, 29]
  },
  "count": 3
}
```
El método `countSimilar` del servicio `SearchCountService` tiene como objetivo contar las búsquedas similares en función de los parámetros de fechas de entrada/salida y edades de los participantes. La consulta utiliza un rango flexible de ±1 día para las fechas y compara las edades de las personas. El criterio es el siguiente:

1. **Fechas**: Se permiten búsquedas similares si las fechas de **checkIn** y **checkOut** están dentro de un margen de ±1 día con respecto a la búsqueda original.
2. **Edades**: Las búsquedas similares se agrupan según el número de personas y sus edades sin importar el orden.
3. **Hotel**: Solo se consideran búsquedas similares dentro del mismo **hotel** (`hotelId`).