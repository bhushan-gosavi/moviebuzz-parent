package com.moviebuzz.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.moviebuzz")
public class MovieBuzzApiApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MovieBuzzApiApplication.class, args);
    }

}
