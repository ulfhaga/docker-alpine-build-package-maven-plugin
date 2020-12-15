#!/bin/sh
set -e
C_DIR=$(dirname "$0")
cd "${C_DIR}"

docker cp target/*-runner.jar server-alpine-jvm:/usr/local/lib/alpine/app.jar
docker restart server-alpine-jvm


