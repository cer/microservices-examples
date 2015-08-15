This project is a web application implemented using [Spring Boot](http://projects.spring.io/spring-boot/).
It's the example code for the article [Building microservices with Spring Boot - part 2](http://plainoldobjects.com/2014/05/05/building-microservices-with-spring-boot-part-2/).

To build and test this webapp, you need to run the backend service and it's dependencies.
If you have Docker compose installed, these two commands will start those services and set the right environment variables.

    $ docker/run-dependencies.sh  
    $ . docker/set-env.sh

Next, build and test the web application:

    $ ./gradlew build    

To package the webapp as a Docker container:

    $ cd docker
    $ ./build.sh

You can then run the webapp using this command:

    $ ./run-webapp.sh

In a browser, you can now open the specified URL.

Next, stop the webapp running by using:

    $ ./stop-webapp.sh


Now that you have built everything you can run all of the services using Netflix OSS Eureka for discovery:

    $ cd ../..
    $ docker-compose up -d

Note - you might need to wait a while for the services to startup and for the backend service to register with [Eureka](https://github.com/Netflix/eureka).

There are a few different URLs that you can visit:

* http://DOCKER_HOST_IP:8080/register.html - registration
* http://DOCKER_HOST_IP:8761 - Eureka console
* http://DOCKER_HOST_IP:8081/swagger-ui.html - the Swagger UI
