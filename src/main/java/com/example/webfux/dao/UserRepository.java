package com.example.webfux.dao;

import com.example.webfux.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User,String>,ReactiveMongoRepository<User,String> {
    Mono<User> findByUsername(String username);
    Mono<Long> deleteByUsername(String username);
    @Tailable
    Flux<User> findBy();

    @Tailable
    Flux<User> findByOrgIdAndPlatformId(String orgId,String platformId);

}
