package com.example.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "item",type = "docs",shards = 1,replicas = 0)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;//标题

    @Field(type = FieldType.Keyword)
    private String category;//分类

    @Field(type = FieldType.Keyword)
    private String brand;//品牌

    @Field(type = FieldType.Double)
    private Double price;//价格

    @Field(type = FieldType.Keyword,index = false)
    private String images;//图片地址


}
