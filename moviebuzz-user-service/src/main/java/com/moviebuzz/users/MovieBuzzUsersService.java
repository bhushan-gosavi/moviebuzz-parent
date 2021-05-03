package com.moviebuzz.users;

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
public class MovieBuzzUsersService
{
    public static void main(String[] args)
    {
        SpringApplication.run(MovieBuzzUsersService.class, args);
    }
}
