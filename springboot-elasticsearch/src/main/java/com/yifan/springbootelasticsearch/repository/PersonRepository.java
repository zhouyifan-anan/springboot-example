package com.yifan.springbootelasticsearch.repository;

import com.yifan.springbootelasticsearch.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created By 周一凡 on
 * 2019/5/21 8:04.
 */
public interface PersonRepository extends ElasticsearchRepository<Person, Long> {

}
