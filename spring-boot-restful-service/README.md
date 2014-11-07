This project is a RESTful web service implemented using [Spring Boot](http://projects.spring.io/spring-boot/).
It's the example code for the article [Building microservices with Spring Boot - part 1](http://plainoldobjects.com/2014/04/01/building-microservices-with-spring-boot-part1/).

In order to run the tests you need RabbitMQ and MongoDB.
An easy way to accomplish that is with Docker.
For example, on MacOSX running boot2docker these two commands will start those services and set the right environment variables.


    $ docker/run-dependencies.sh  
    $ . docker/set-env.sh

To package the service as a Docker container:

    $ cd docker
    $ ./build.sh
    
You can then run the service using this command:

    $ ./run-service.sh
    
And, send a registration request using:

    $ ./register-user.sh
    






