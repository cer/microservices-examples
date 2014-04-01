package net.chrisrichardson.microservices.restfulspringboot.backend

import org.springframework.data.mongodb.repository.MongoRepository

trait RegisteredUserRepository extends MongoRepository[RegisteredUser, String]