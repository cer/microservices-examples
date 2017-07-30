FROM java:openjdk-8u91-jdk
CMD java -jar zipkin-server.jar --server.port=9411
EXPOSE 8080
COPY build/libs/zipkin-server.jar .
