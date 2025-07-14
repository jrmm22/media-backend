
# media-backend

### Introduction

This is a sample application that encodes existing video files into web-friendly HLS `.m3u8` playlists for browser playback.
It is intended to demonstrate:

- A simple job orchestration pattern in Java
- REST endpoints for triggering jobs, monitoring progress, and exposing metrics
- Integration between Java, FFmpeg, and a persistent database
- Dockerized services that can be extended independently

The application simulates how video encoding and delivery systems are structured in real-world media platforms.

---

### How to Build

Build all required Docker images with:

```bash
make -C build
```

This will generate:

`base_image:latest` – Contains shared FFmpeg/Java setup

`rest_image:latest` – Exposes REST API for job submission and monitoring

### How to Run
You can run each image independently, or start the entire system using Docker Compose:

```bash
docker compose up -d
```

This will start:

The REST server (`rest_image`)

Any other defined services (e.g., `encoder`, `metrics exporter`, `database`)

### REST Endpoints
Once running, you can access:

- http://localhost:8080/trigger — Submit a new encoding job
- http://localhost:8080/status — View active job pool status
- http://localhost:8080/metrics — View counters and stats
