FROM openjdk:8u171-jre-alpine
RUN apk --no-cache add curl
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:9411 || exit 1
CMD java -jar zipkin-server.jar --server.port=9411
EXPOSE 9411
COPY build/libs/zipkin-server.jar .
