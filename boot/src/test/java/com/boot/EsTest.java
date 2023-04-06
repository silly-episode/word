package com.boot;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ShardStatistics;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/25 15:34
 * @FileName: EsTest
 * @Description: test
 */
@SpringBootTest
public class EsTest {

    @Autowired
    ElasticsearchClient elasticsearchClient;



    @Test
    public void createIndex() {

    }


    @Test
    public void wordTest() throws IOException {
        File file = new File("src/main/resources/static/wordResources/test.json");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readValue(file, Map.class));

    }


    @Test
    public void test() throws IOException {

        //删除一个索引
        DeleteIndexResponse delete = elasticsearchClient.indices().delete(f ->
                f.index("cet-4")
        );
        System.out.println("delete.acknowledged() = " + delete.acknowledged());
    }

    @Test
    public void wordTest2() throws IOException {
        String word = "";

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);

        //open the file
        FileReader file = new FileReader("src/main/resources/static/wordResources/test.json");
        BufferedReader buffer = new BufferedReader(file);
        ObjectMapper mapper = new ObjectMapper();
//        while (word!=null) {
//            word = buffer.readLine();
//            if (word!=null){
//                StringReader sr = new StringReader(word);
//                IndexResponse response = client.index(i -> i.index("cet-4").withJson(sr).id("1"));
//            }
//        }
        word = buffer.readLine();
        StringReader sr = new StringReader(word);
        IndexResponse response = client.index(i -> i.index("cet-4").withJson(sr).id(String.valueOf(1)));
        SearchResponse<ObjectNode> search = client.search(g -> g
                        .index("cet-4"),
                ObjectNode.class
        );
        System.out.println(search.toString());
    }


    @Test
    public void select() throws IOException {

        SearchRequest request = new SearchRequest.Builder()
                .index("英语四级")
                .query(QueryBuilders.matchAll().build()._toQuery())
                .from(0)
                .size(10)
                .build();
        //        查询体
        SearchResponse<ObjectNode> search = elasticsearchClient.search(request, ObjectNode.class);
        System.out.println("search.toString() = " + search.toString());
//
        long took = search.took();
        System.out.println("took = " + took);
//        是否超时
        boolean b = search.timedOut();
        System.out.println("b = " + b);
//        分片信息
        ShardStatistics shards = search.shards();
        System.out.println("shards = " + shards);
//        hit
        HitsMetadata<ObjectNode> hits = search.hits();
//        total
        TotalHits total = hits.total();
        System.out.println("total = " + total);
//        评分
        Double maxScore = hits.maxScore();
        System.out.println("maxScore = " + maxScore);
//        hit中的hit
        List<Hit<ObjectNode>> list = hits.hits();
        for (Hit<ObjectNode> bookHit : list) {
//            source
            System.out.println("bookHit.source() = " + bookHit.source());
//            score
            System.out.println("bookHit.score() = " + bookHit.score());
//            index
            System.out.println("bookHit.index() = " + bookHit.index());
        }
    }
}
