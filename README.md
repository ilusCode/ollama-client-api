# 🧠 Ollama Client API

API REST en **Spring Boot 4** que expone un endpoint de chat sencillo sobre un servidor **local** de
[Ollama](https://ollama.com). Recibe un mensaje, se lo envía al modelo (por defecto `llama3.2`) y
devuelve la respuesta generada. Todo se ejecuta en tu máquina, sin enviar datos a servicios externos.

![Java](https://img.shields.io/badge/Java-26-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![WebFlux](https://img.shields.io/badge/Spring-WebFlux-6DB33F?logo=spring&logoColor=white)
![Ollama](https://img.shields.io/badge/Ollama-local_LLM-000000?logo=ollama&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

---

## 🏗️ Cómo funciona

```
Cliente ──POST /api/chat──▶ Ollama Client API (Spring Boot :8080)
                                   │  WebClient
                                   ▼
                           Ollama (:11434) POST /api/generate ──▶ llama3.2
```

| Capa | Clase | Responsabilidad |
|------|-------|-----------------|
| Controller | `OllamaController` | Expone `POST /api/chat` |
| Service | `ConversationService` (`IConversationService`) | Construye la petición a Ollama y transforma la respuesta |
| Config | `WebClientConfig` | Crea el `WebClient` apuntando a `host.ollama-service` |
| Model | `OllamaClientRequestBody` / `OllamaClientResponseBody` | Contrato público de la API |
| Model | `OllamRequest` / `OllamaResponse` | Contrato con la API de Ollama |

## 📡 API

### `POST /api/chat`

**Request**

```json
{
  "message": "¿Qué es Docker?"
}
```

**Response `200 OK`**

```json
{
  "message": "Docker es una plataforma que permite empaquetar aplicaciones en contenedores...",
  "time": "2026-10-29T12:30:45.123456"
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `message` | `string` | Texto generado por el modelo |
| `time` | `string` | Fecha y hora (ISO-8601, hora local del servidor) en que se recibió la respuesta |

**Ejemplo con curl**

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Explica la IA en una frase"}'
```

> Si Ollama no está corriendo o el modelo no está descargado, la API responde con un error `500`.

## 📋 Requisitos

- **Java 26**
- **Maven 3.9+** (el proyecto no incluye Maven Wrapper)
- **Docker** y Docker Compose, para levantar Ollama
- Mínimo 8 GB de RAM (16 GB recomendado) y ~3 GB libres para el modelo `llama3.2`

## 🚀 Puesta en marcha

### 1. Levantar Ollama

El repositorio incluye [`docs/docker-compose.yml`](docs/docker-compose.yml):

```bash
cd docs
docker compose up -d
```

### 2. Descargar el modelo

```bash
docker exec -it ollama-server ollama pull llama3.2
```

Comprueba que Ollama responde:

```bash
curl http://localhost:11434/api/generate \
  -d '{"model": "llama3.2", "prompt": "Di hola mundo", "stream": false}'
```

### 3. Ejecutar la API

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

> **IntelliJ IDEA:** en [`docs/launcher/`](docs/launcher) hay una configuración de ejecución lista para importar.

## ⚙️ Configuración

`src/main/resources/application.yaml`

```yaml
api:
  ia:
    model: llama3.2        # Modelo de Ollama a usar
    stream: false          # Respuesta completa (no streaming)

host:
  ollama-service: "http://127.0.0.1:11434"   # URL del servidor Ollama

service:
  ollama-service: /api/generate              # Endpoint de Ollama
```

Cualquier propiedad se puede sobrescribir al arrancar, por ejemplo para usar otro modelo:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--api.ia.model=llama3.2:1b"
```

### Modelos sugeridos

| Modelo | Tamaño | RAM mínima | Uso |
|--------|--------|------------|-----|
| `llama3.2:1b` | 1.3 GB | ~4 GB | Pruebas, respuestas rápidas |
| `llama3.2` (3b) | 2.0 GB | ~8 GB | Uso diario (por defecto) |
| `gemma2:2b` | 1.6 GB | ~6 GB | Alternativa ligera |
| `llama3.1:8b` | 4.9 GB | ~16 GB | Tareas más complejas |

## 🔧 Comandos útiles de Ollama

```bash
docker logs -f ollama-server                       # Ver logs
docker exec -it ollama-server ollama list          # Modelos descargados
docker exec -it ollama-server ollama rm llama3.2   # Eliminar un modelo
docker compose -f docs/docker-compose.yml down     # Detener Ollama
```

### Aceleración con GPU (NVIDIA)

Con el [NVIDIA Container Toolkit](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html)
instalado, agrega esto al servicio `ollama` de `docker-compose.yml`:

```yaml
    deploy:
      resources:
        reservations:
          devices:
            - driver: nvidia
              count: all
              capabilities: [gpu]
```

## ⚠️ Solución de problemas

| Problema | Solución |
|----------|----------|
| `Connection refused` al llamar a `/api/chat` | Ollama no está corriendo: `docker compose up -d` en `docs/` |
| Ollama responde `model not found` | Descarga el modelo: `docker exec -it ollama-server ollama pull llama3.2` |
| `port is already allocated` (11434) | Cambia el puerto externo en `docker-compose.yml` (`"11435:11434"`) y en `host.ollama-service` |
| Respuestas muy lentas | Usa un modelo más pequeño (`llama3.2:1b`) o activa la GPU |

## 📁 Estructura

```
├── docs/
│   ├── docker-compose.yml          # Servidor Ollama
│   └── launcher/                   # Configuración de ejecución para IntelliJ
├── src/main/java/com/iluscode/ollama_client_api/
│   ├── config/WebClientConfig.java
│   ├── controller/OllamaController.java
│   ├── model/
│   └── service/
├── src/main/resources/application.yaml
└── pom.xml
```

## 👤 Autor

**Eduardo Fernández Morales** — [@ilusCode](https://github.com/ilusCode) · [LinkedIn](https://www.linkedin.com/in/eduardo-fernandez-morales/)
