package com.boot;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
    public void wordTest()throws IOException {
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
        while (word!=null) {
            word = buffer.readLine();
            if (word!=null){
                StringReader sr = new StringReader(word);
                IndexResponse response = client.index(i -> i.index("cet-4").withJson(sr).id("1"));
            }
        }
        word = buffer.readLine();
        StringReader sr = new StringReader(word);
        IndexResponse response = client.index(i -> i.index("cet-4").withJson(sr));
        SearchResponse<ObjectNode> search = client.search(g -> g
                        .index("cet-4"),
                ObjectNode.class
        );
        System.out.println(search.toString());
    }
}
