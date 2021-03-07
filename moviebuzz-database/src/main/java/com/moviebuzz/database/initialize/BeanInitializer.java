package com.moviebuzz.database.initialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitializer
{

    private class DateTypeAdapter extends TypeAdapter<Date>
    {
        @Override
        public void write(JsonWriter out, Date value) throws IOException
        {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value.getTime() / 1000);
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            if (in == null) {
                return null;
            }
            return new Date(in.nextLong() * 1000);
        }
    }
    @Bean
    public ObjectMapper getObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    @Bean
    public Gson getGson()
    {
        return new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    }
}
