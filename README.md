🧠 Ollama Local con Docker

Guía paso a paso para levantar un servidor de IA local usando Ollama dentro de contenedores Docker.

📋 Requisitos Previos

Docker y Docker Compose instalados

Instalar Docker Desktop (Windows/Mac)
Instalar Docker Engine (Linux)
Mínimo 8 GB de RAM (16 GB recomendado)
~8 GB de espacio libre en disco para modelos
Conexión a internet (solo para la descarga inicial del modelo)
🚀 Instalación Rápida

1. Clonar o crear el proyecto

bash
mkdir ollama-local
cd ollama-local
2. Crear el archivo docker-compose.yml

yaml
services:
ollama:
image: ollama/ollama:latest
container_name: ollama-server
ports:
- "11434:11434"
volumes:
- ./ollama_data:/root/.ollama
restart: unless-stopped
environment:
- OLLAMA_KEEP_ALIVE=24h
- OLLAMA_HOST=0.0.0.0
3. Levantar el servidor

bash
docker compose up -d
4. Descargar un modelo

bash
# Modelo pequeño y rápido (recomendado para empezar)
docker exec -it ollama-server ollama pull llama3.2:1b

# Modelo equilibrado (calidad/rendimiento)
docker exec -it ollama-server ollama pull llama3.2:3b

# Modelo más potente (requiere más RAM)
docker exec -it ollama-server ollama pull llama3.1:8b
5. Probar la instalación

bash
curl -X POST http://localhost:11434/api/generate \
-H "Content-Type: application/json" \
-d '{"model": "llama3.2:1b", "prompt": "Di Hola mundo", "stream": false}'
🎯 Modelos Recomendados

Modelo	Tamaño	RAM Mínima	Uso Recomendado
llama3.2:1b	1.3 GB	~4 GB	Pruebas, respuestas rápidas
llama3.2:3b	2.8 GB	~8 GB	Uso diario, buen rendimiento
gemma2:2b	2.1 GB	~6 GB	Alternativa ligera
llama3.1:8b	8.5 GB	~16 GB	Tareas complejas
qwen2.5:14b	14 GB	~24 GB	Alto rendimiento
🔧 Comandos Útiles

Gestión del contenedor

bash
# Ver estado del contenedor
docker ps | grep ollama

# Ver logs
docker logs -f ollama-server

# Detener el servidor
docker compose down

# Reiniciar el servidor
docker compose restart

# Actualizar a la última versión
docker compose pull
docker compose up -d
Gestión de modelos

bash
# Listar modelos descargados
docker exec -it ollama-server ollama list

# Eliminar un modelo
docker exec -it ollama-server ollama rm llama3.2:1b

# Ver información de un modelo
docker exec -it ollama-server ollama show llama3.2:1b
Interacción desde terminal

bash
# Modo interactivo (chat)
docker exec -it ollama-server ollama run llama3.2:1b

# Consulta única
docker exec -it ollama-server ollama run llama3.2:1b "¿Qué es Docker?"
💻 Uso desde código

Python

python
import requests
import json

response = requests.post(
'http://localhost:11434/api/generate',
json={
'model': 'llama3.2:1b',
'prompt': 'Explica la IA en una frase',
'stream': False
}
)

print(json.loads(response.text)['response'])
JavaScript / Node.js

javascript
const response = await fetch('http://localhost:11434/api/generate', {
method: 'POST',
headers: { 'Content-Type': 'application/json' },
body: JSON.stringify({
model: 'llama3.2:1b',
prompt: 'Explica la IA en una frase',
stream: false
})
});

const data = await response.json();
console.log(data.response);
⚡ Aceleración con GPU (NVIDIA)

En Linux

bash
# Instalar NVIDIA Container Toolkit
distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | sudo apt-key add -
curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | sudo tee /etc/apt/sources.list.d/nvidia-docker.list

sudo apt-get update && sudo apt-get install -y nvidia-container-toolkit
sudo systemctl restart docker
Modificar docker-compose.yml para GPU

yaml
services:
ollama:
image: ollama/ollama:latest
container_name: ollama-server
ports:
- "11434:11434"
volumes:
- ./ollama_data:/root/.ollama
restart: unless-stopped
deploy:
resources:
reservations:
devices:
- driver: nvidia
count: all
capabilities: [gpu]
🗑️ Desinstalación Completa

bash
# Detener y eliminar contenedor
docker compose down -v

# Eliminar datos de modelos (opcional)
rm -rf ./ollama_data

# Eliminar imagen (opcional)
docker rmi ollama/ollama:latest
📚 Recursos Adicionales

Biblioteca de modelos de Ollama
Documentación oficial de la API
Modelos recomendados según hardware
⚠️ Solución de Problemas

Error: "port is already allocated"

bash
# Cambiar el puerto en docker-compose.yml
ports:
- "11435:11434"  # Cambia el puerto externo

# O detener el proceso que usa el puerto
sudo lsof -i :11434
kill -9 [PID]
Error: "no space left on device"

bash
# Limpiar modelos no usados
docker exec -it ollama-server ollama prune

# Eliminar contenedores/volúmenes huérfanos
docker system prune -f
El modelo es muy lento

bash
# Usar un modelo más pequeño
docker exec -it ollama-server ollama pull llama3.2:1b

# Reducir el contexto (menos memoria)
curl -X POST http://localhost:11434/api/generate \
-H "Content-Type: application/json" \
-d '{"model": "llama3.2:3b", "prompt": "Hola", "options": {"num_ctx": 512}}'
📝 Notas Importantes

Todos los datos se almacenan localmente en la carpeta ./ollama_data
No se envían datos a servicios externos
Puedes usar Ollama sin conexión a internet después de descargar los modelos
El tiempo de respuesta depende de tu hardware (CPU/GPU)
¿Necesitas ayuda? Abre un issue o consulta la documentación oficial.