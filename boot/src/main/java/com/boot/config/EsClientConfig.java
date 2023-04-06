package com.boot.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/26 14:05
 * @FileName: EsClientConfig
 * @Description: Es客户端配置
 */
@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
public class EsClientConfig {
    @Setter
    private String host;

    /**
     * @Return: HttpHost
     * @Author: DengYinzhe
     * @Description: 解析配置的字符串，转为HttpHost对象数组
     * @Date: 2023/2/9 9:06
     */
    private HttpHost[] toHttpHost() {
        if (!StringUtils.hasLength(host)) {
            throw new RuntimeException("invalid elasticsearch configuration");
        }
        String[] hostArray = host.split(",");
        HttpHost[] httpHosts = new HttpHost[hostArray.length];
        HttpHost httpHost;
        for (int i = 0; i < hostArray.length; i++) {
            String[] strings = hostArray[i].split(":");
            httpHost = new HttpHost(strings[0], Integer.parseInt(strings[1]), "http");
            httpHosts[i] = httpHost;
        }

        return httpHosts;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        HttpHost[] httpHosts = toHttpHost();
        RestClient restClient = RestClient.builder(httpHosts).build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchAsyncClient elasticsearchAsyncClient() {
        HttpHost[] httpHosts = toHttpHost();
        RestClient restClient = RestClient.builder(httpHosts).build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchAsyncClient(transport);
    }
}

