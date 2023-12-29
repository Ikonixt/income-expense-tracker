#!/bin/bash/

docker run -p 127.0.0.1:13306:3306 --name ssc_project --env MARIADB_ROOT_PASSWORD=root -d --restart=always mariadb:latest
