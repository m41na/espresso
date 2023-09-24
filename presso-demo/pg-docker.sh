docker run -d \
  --name demo-postgres \
  --restart always \
  -v pgdata:/var/lib/postgresql/data \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres
