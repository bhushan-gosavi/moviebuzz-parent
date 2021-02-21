package com.moviebuzz.database.elasticsearch.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@EnableAutoConfiguration(exclude={ElasticsearchRestClientAutoConfiguration.class})
public class ElasticsearchConfig
{
    @Value("${elasticsearch.hosts}")
    private String elasticsearchHost;

    @Value("${elasticsearch.cluster.name}")
    private String elasticsearchClusterName;

    @Value("${elasticsearch.client.transport.ping_timeout}")
    private String transportPingTimeout;

    @Value("${elasticsearch.client.transport.nodes_sampler_interval}")
    private String transportNodeSamplerInterval;


    @Bean
    public Client client() {

        Settings.Builder settingsBuilder = Settings.builder();
        settingsBuilder.put("cluster.name", elasticsearchClusterName);
        settingsBuilder.put("client.transport.ping_timeout", transportPingTimeout);
        settingsBuilder.put("client.transport.nodes_sampler_interval", transportNodeSamplerInterval);

        System.setProperty("es.set.netty.runtime.available.processors", "false");

        TransportClient client = new PreBuiltTransportClient(settingsBuilder.build());

        for (String host : elasticsearchHost.split(",")) {
            String[] pairs = host.split(":");
            int port = pairs.length == 2 ? Integer.valueOf(pairs[1]) : 9300;
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(pairs[0]), port));
            } catch (UnknownHostException e) {
                throw new RuntimeException("Error in connecting to Elasticsearch " + host, e);
            }
        }

        return client;
    }
}
