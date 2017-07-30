#! /bin/bash -e

./gradlew assemble
docker build -t sb_web .
