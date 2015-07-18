#! /bin/bash -e

rm -fr build
mkdir build
cp ../target/spring-boot-user-registration-webapp-1.0-SNAPSHOT.jar build

docker build -t sb_web .
