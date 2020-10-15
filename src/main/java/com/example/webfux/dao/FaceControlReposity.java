package com.example.webfux.dao;

import com.example.webfux.entity.FaceControlData;
import com.example.webfux.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FaceControlReposity extends ReactiveCrudRepository<FaceControlData,String>, ReactiveMongoRepository<FaceControlData,String> {
    @Tailable
    Flux<FaceControlData> findByOrgId(String orgId);
    Flux<FaceControlData> findBy();
}
