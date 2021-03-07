package com.moviebuzz.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.moviebuzz")
public class MovieBuzzProcessorApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MovieBuzzProcessorApplication.class, args);
    }
}
