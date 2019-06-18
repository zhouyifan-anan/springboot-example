package com.yifan.springbootelasticsearch.repository;

import com.alibaba.fastjson.JSON;
import com.yifan.springbootelasticsearch.SpringbootElasticsearchApplicationTests;
import com.yifan.springbootelasticsearch.entity.Person;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String preTag = "<em>";//google的色值
        String postTag = "</em>";
        String str = "天津";
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", str))
                //.withQuery(QueryBuilders.matchQuery("city", str))
                .withHighlightFields(new HighlightBuilder.Field("name").preTags(preTag).postTags(postTag),
                                     new HighlightBuilder.Field("city").preTags(preTag).postTags(postTag))
                .build();

        //Page<Person> person = personRepository.search(searchQuery);


        // 高亮字段
        AggregatedPage<Person> people = elasticsearchTemplate.queryForPage(searchQuery, Person.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Person> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }

                    String objJsonStr = searchHit.getSourceAsString();
                    //todo 序列化为实体对象


                    Person person1 = JSON.parseObject(objJsonStr, Person.class);
                    //name or memoe
                    HighlightField name = searchHit.getHighlightFields().get("name");
                    if (name != null) {
                        person1.setName(name.fragments()[0].toString());
                    }
                    HighlightField city = searchHit.getHighlightFields().get("city");
                    if (city != null) {
                        person1.setCity(city.fragments()[0].toString());
                    }
                    chunk.add(person1);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return null;
            }
        });

        System.out.println(people.getTotalElements());

        for (Person p : people) {
            System.out.println(p);
        }
    }


}