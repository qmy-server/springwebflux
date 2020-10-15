package com.example.webfux.service;

import com.alibaba.fastjson.JSONObject;
import com.example.webfux.entity.FaceControlData;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class FaceControlService {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    //查询业务数据（按照单位ID查询-今日）
    public Flux<String> findAllFace(String ogrid) {
        Flux<String> dataFlux = null;

        Query query = new Query();
        Disposable disposable;
        //query.addCriteria(Criteria.where("createTime").regex("2020-09-18 10"));
        query.addCriteria(Criteria.where("orgId").is(ogrid));
        String orgTabelName = "org_13000400_faceControlData";
        //query为查询条件；FaceControlData.class为接收到新数据时映射到的实体类；orgTabelName为自定义的数据库集合名称


            dataFlux = reactiveMongoTemplate.tail(query, FaceControlData.class, orgTabelName)
                    .map(this::tranfer)
                    .doOnCancel(() -> {
                        System.out.println("流被取消");
                    })
                    //.doOnSubscribe(Subscription::cancel)
                    .doFinally(d -> {
                        System.out.println("最终方法");

                    });
        return dataFlux;

    }

    private String tranfer(FaceControlData v) {
        JSONObject j = JSONObject.parseObject(v.getData());
        v.setDataSource(j);
        v.setData(null);
        return JSONObject.toJSONString(v);


    }
}
