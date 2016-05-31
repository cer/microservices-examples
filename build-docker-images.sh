#! /bin/bash

set -e

(cd spring-boot-restful-service/docker ; ./build.sh)
(cd  spring-boot-webapp/docker ; ./build.sh)
