#! /bin/bash -e

./wait-for-running-system.sh

echo The microservices are running
echo You can visit these URLS
echo http://${DOCKER_HOST_IP?}:8080/register.html - registration UI
echo http://${DOCKER_HOST_IP?}:8761 - Eureka console
echo http://${DOCKER_HOST_IP?}:8081/swagger-ui.html - the Backend Swagger UI
echo http://${DOCKER_HOST_IP?}:9411 - Zipkin
echo http://${DOCKER_HOST_IP?}:15672 - RabbitMQ admin - guest/guest login
