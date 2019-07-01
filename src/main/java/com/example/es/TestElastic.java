package com.example.es;


import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestElastic {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * 创建索引库
     */
    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Item.class);
        elasticsearchTemplate.putMapping(Item.class);
    }

    /**
     * 保存数据
     */
    @Test
    public void saveItem(){
        Item item1 = new Item(2L,"大米手机1","手机","大米",3499.00,"http://www.baidu.com");
        Item item2 = new Item(3L,"大米手机","手机","大米",4499.00,"http://www.baidu.com");
        Item item3 = new Item(4L,"华为p30","手机","华为",3999.00,"http://www.baidu.com");
        Item item4 = new Item(5L,"华为8","手机","华为",1999.00,"http://www.baidu.com");
        List<Item> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        itemRepository.saveAll(list);
    }

    /**
     * 查询所有数据  分页排序
     */
    @Test
    public void findAll(){
        Iterable<Item> all = itemRepository.findAll();
        Iterator<Item> iterator = all.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

        System.out.println("==============分页查询  默认从第0页开始");
        Page<Item> itemList = itemRepository.findAll(PageRequest.of(1, 3));
        for(Item item:itemList){
            System.out.println(item);
        }

        System.out.println("==============排序查询");

        Iterable<Item> itemOrder = itemRepository.findAll(Sort.by("price").descending());
        Iterator<Item> iterator1 = itemOrder.iterator();
        while (iterator1.hasNext()){
            System.out.println(iterator1.next());
        }
    }

    /**
     * 自定义查询
     */
    @Test
    public void search(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","小米"));
        Page<Item> search = itemRepository.search(queryBuilder.build());
        // 获取总条数
        long total = search.getTotalElements();
        System.out.println("总的数据条数为:"+total);
        for(Item item:search){
            System.out.println(item);
        }
    }

    /**
     * 条件查询
     */
    @Test
    public void searchAndSort(){
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "华为"));

        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);

        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 范围查询
     */
    @Test
    public void testRangeQuery(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //        queryBuilder.withQuery(QueryBuilders.fuzzyQuery("title","小目"));


        queryBuilder.withQuery(QueryBuilders.rangeQuery("price").from(3000).to(4000));

        Page<Item> page = itemRepository.search(queryBuilder.build());

        for(Item i:page){
            System.out.println(i);
        }


    }
}
