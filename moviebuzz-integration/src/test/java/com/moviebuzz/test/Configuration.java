package com.moviebuzz.test;

import java.io.IOException;
import java.util.Properties;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

@Getter
public class Configuration
{
    private String apiServer;
    private String apiPort;

    private static final Configuration configuration = null;

    public static Configuration getInstance() throws IOException
    {
        if(configuration == null)
        {
            return new Configuration();
        }
        return configuration;
    }

    private Configuration() throws IOException
    {
        Properties prop = new Properties();
        prop.load(new ClassPathResource("application.properties").getInputStream());
        apiServer = prop.getProperty("moviebuzz.api.server");
        apiPort = prop.getProperty("moviebuzz.api.port");
    }
}
