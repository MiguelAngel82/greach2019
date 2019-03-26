# Java Frameworks Comparison

Description.

## Prometheus

To run prometehus in Docker:

```bash
docker run -d -p 9090:9090 --mount type=bind,source="$(pwd)"/prometheus/prometheus.yml,target=/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
```
