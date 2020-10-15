package com.example.webfux.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class FaceControlData {
    @Id
    private ObjectId id;
    private String data;
    private String createTime;
    private String robotId;
    private String platformId;
    private String orgId;
    private Integer dataId;
    //转义data,由于我们Mongodb数据存的data字段是json字符串类型。所以我这里把data字段的内容进行了转义。
    //如果Mongodb数据库集合中的字段都是基本数据类型。此定义的对象可省略
    private JSONObject dataSource;
}
