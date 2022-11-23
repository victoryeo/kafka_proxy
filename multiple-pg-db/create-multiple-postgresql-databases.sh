#!/bin/bash

set -e
set -u

function create_user_and_database() {
	local database=$1
	echo "  Creating user and database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE "$database";
	    GRANT ALL PRIVILEGES ON DATABASE "$database" TO postgres;
EOSQL
}

function create_user_and_database_and_initdata() {
	local database=$1
	echo "  Creating user and database '$database'"
	echo "  User '$POSTGRES_USER'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE $database;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO postgres;
			\connect $database
			CREATE TABLE "price" (id serial PRIMARY KEY, price real, timeoftrade VARCHAR (255), tokenname VARCHAR (255), tokentype VARCHAR (255));
			INSERT INTO "price" (id, price, timeoftrade, tokenname, tokentype) VALUES (1, 1.1, '2022-11-04T10:14:03.045657921+08:00', 'JOU', 'Stock');
			INSERT INTO "price" (id, price, timeoftrade, tokenname, tokentype) VALUES (2, 1.4, '2022-11-04T10:14:03.045657921+08:00', 'AAPL', 'Stock');
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
	for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
	  if [ "price" = "$db" ]; then
		  create_user_and_database_and_initdata $db
    else
		  create_user_and_database $db
	  fi
	done
	echo "Multiple databases created"
fi
