docker run -d -p 8080:8080 \
-e   SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP}/userregistration  \
-e SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP} \
sb_rest_svc
