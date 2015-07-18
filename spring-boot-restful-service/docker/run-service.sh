docker run -d -p 8081:8080 \
-e SPRING_DATA_MONGODB_URI  \
-e SPRING_RABBITMQ_HOST \
--name sb_rest_svc \
sb_rest_svc
