package com.yifan.springbootelasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created By 周一凡 on
 * 2019/5/21 7:51.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "persons", type = "person")
public class Person {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer age;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private List<String> favorites;


}
