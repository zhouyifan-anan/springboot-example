package com.yifan.springbootelasticsearch.repository;

import com.yifan.springbootelasticsearch.SpringbootElasticsearchApplicationTests;
import com.yifan.springbootelasticsearch.entity.Person;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Arrays;

/**
 * <B>文件名称：</B> <BR>
 * <B>文件描述：</B> <BR>
 * <BR>
 * <B>版权声明：</B>(C)2019-2018<BR>
 * <B>公司部门：</B>博汇科技  事业一部<BR>
 * <B>创建时间：</B>2019-05-21 8:05<BR>
 *
 * @author 周一凡
 * @version 2.1.0.1
 */
public class PersonRepositoryTest extends SpringbootElasticsearchApplicationTests {


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Test
    public void createIndex() {

        Person person = new Person();
        person.setId(1L);
        person.setName("我爱北京天安门");
        person.setAge(70);
        person.setCity("我爱北京，我当然在北京咯");
        person.setFavorites(Arrays.asList("天安门", "长城", "故宫"));

        personRepository.save(person);

    }

    @Test
    public void createIndexs() {

        Person person = new Person(2L, "我爱天津", 71, "我爱天津，我当然在天津", Arrays.asList("天津之眼", "意大利风情区"));
        Person person1 = new Person(3L, "我爱青岛", 72, "我爱青岛，我当然在青岛", Arrays.asList("沙滩", "青岛啤酒"));
        personRepository.saveAll(Arrays.asList(person, person1));

    }

    @Test
    public void searchAll() {
        String str = "天津";
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", str))
                .withFilter(QueryBuilders.matchPhraseQuery("id", "1"))
                .build();

        Page<Person> person = personRepository.search(searchQuery);
        System.out.println(person.getTotalElements());

        for (Person p : person) {
            System.out.println(p);
        }

    }


}