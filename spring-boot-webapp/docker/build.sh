#! /bin/bash -e

rm -fr build
mkdir build
cp ../build/libs/spring-boot-webapp.jar build

docker build -t sb_web .
