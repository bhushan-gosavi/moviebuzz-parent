package com.moviebuzz.front.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.moviebuzz")
@EnableAutoConfiguration(exclude={ElasticsearchRestClientAutoConfiguration.class})
public class MovieBuzzFrontAPI
{
    public static void main(String[] args)
    {
        SpringApplication.run(MovieBuzzFrontAPI.class, args);
    }
}
