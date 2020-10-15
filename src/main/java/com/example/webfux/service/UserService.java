package com.example.webfux.service;

import com.example.webfux.dao.FaceControlReposity;
import com.example.webfux.dao.UserRepository;
import com.example.webfux.entity.FaceControlData;
import com.example.webfux.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FaceControlReposity faceControlReposity;



    /**
     * 保存或更新。
     * 如果传入的user没有id属性，由于username是unique的，在重复的情况下有可能报错，
     * 这时找到以保存的user记录用传入的user更新它。
     */
    public Mono<User> save(User user) {
        return userRepository.save(user)
                .onErrorResume(e ->     // 1
                        userRepository.findByUsername(user.getUsername())   // 2
                                .flatMap(originalUser -> {      // 4
                                    user.setId(originalUser.getId());
                                    return userRepository.save(user);   // 3
                                }));
    }

    public Mono<Long> deleteByUsername(String username) {
        return userRepository.deleteByUsername(username);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Flux<User> findAll() {
        Sort sort=Sort.by("id").descending();
        PageRequest pageRequest=PageRequest.of(0, 3,sort);
        //Flux<User> userFlux=userRepository.findBy();

        Flux<User> userFlux=userRepository.findAll(sort).take(5);

        return userFlux;
        //return userRepository.findBy();

    }
    //查询业务数据（按照单位ID查询）
    public Flux<FaceControlData> findAllFace() {
        //Sort sort=Sort.by("dataId").descending();
        //PageRequest pageRequest=PageRequest.of(0, 3,sort);

        //条件查询,,Example匹配条件
        FaceControlData faceControlData=new FaceControlData();
        faceControlData.setOrgId("13000400");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("orgId", ExampleMatcher.GenericPropertyMatchers.contains());
                /*.withIgnorePaths("orgId")
                .withIgnorePaths("_id")
                .withIgnorePaths("data")
                .withIgnorePaths("createTime")
                .withIgnorePaths("platformId")
                .withIgnorePaths("dataId");*/
        Example<FaceControlData> faceControlDataExample=Example.of(faceControlData,matcher);
        Mono<Long> num=faceControlReposity.count(faceControlDataExample);
            //一个数据集合100条，砍掉95条数据，留下5条最新数据返回给前端
        Flux<FaceControlData> userFlux=faceControlReposity.findByOrgId("13000400").skip(95);
       return userFlux;


    }

}
