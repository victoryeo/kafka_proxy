FROM bitnami/postgresql:15.1.0-debian-11-r0
COPY create-multiple-postgresql-databases.sh /docker-entrypoint-initdb.d/
COPY seed.csv . 
