package com.moviebuzz.database.cassandra.config;


import info.archinnov.achilles.generated.ManagerFactory;
import info.archinnov.achilles.generated.manager.MovieEntity_Manager;
import info.archinnov.achilles.generated.manager.UserEntity_Manager;
import info.archinnov.achilles.generated.manager.UserRatingEntity_Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
/**
 * This Configuration class creates a EntityManager Bean for each Cassandra Table.
 * All the services will leverage these EntityManagers to communicate with Cassandra.
 * Whenever we add new Cassandra table, we should initialize it's EntityManager Bean here
 * https://github.com/doanduyhai/Achilles
 */
public class ManagerFactoryConfig
{

    @Autowired
    private ManagerFactory managerFactory;

    @Bean
    public MovieEntity_Manager getMovieManager()
    {
        return managerFactory.forMovieEntity();
    }

    @Bean
    public UserEntity_Manager getUserManager()
    {
        return managerFactory.forUserEntity();
    }

    @Bean
    public UserRatingEntity_Manager getUserRatingManager()
    {
        return managerFactory.forUserRatingEntity();
    }
}