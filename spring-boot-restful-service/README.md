This project is a RESTful web service implemented using [Spring Boot](http://projects.spring.io/spring-boot/).
It's the example code for the article [Building microservices with Spring Boot - part 1](http://plainoldobjects.com/2014/04/01/building-microservices-with-spring-boot-part1/).

In order to run the tests you need RabbitMQ and MongoDB.
An easy way to accomplish that is with Docker.
For example, if you have [docker-compose](https://docs.docker.com/compose/) installed (and  [boot2docker](http://boot2docker.io/) on a non-Linux machine) these two commands will start those services and set the appropriate environment variables.


    $ docker/run-dependencies.sh  
    $ . docker/set-env.sh

Next, build and test the service:

    $ ./gradlew build    

To package the service as a Docker container:

    $ cd docker
    $ ./build.sh
    
You can then run the service using this command:

    $ ./run-service.sh
    
And, send a registration request using:

    $ ./register-user.sh
    
You can look at the MongoDB database using the following commands

```
$ ../../mongodb-cli.sh 
> show dbs;
local             0.031GB
mydb              0.031GB
userregistration  0.031GB
> use userregistration;
switched to db userregistration
> 
> 
> show collections;
registeredUser
system.indexes
> 
> 
> db.registeredUser.find()
{ "_id" : ObjectId("55a99b0993860551c6020e9d"), "_class" : "net.chrisrichardson.microservices.restfulspringboot.backend.RegisteredUser", "emailAddress" : "1437178632863-b-foo@bar.com", "password" : "secret" }
> exit
$
```

 Next, stop the service running by using:

    $ ./stop-service.sh

The next step is to [build and test the web application](../spring-boot-webapp).

    






