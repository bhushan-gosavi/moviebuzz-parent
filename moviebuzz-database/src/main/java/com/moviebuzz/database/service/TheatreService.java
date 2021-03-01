package com.moviebuzz.database.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebuzz.database.cassandra.models.TheatreEntity;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping;
import com.moviebuzz.database.elasticsearch.index.EsIndexMapping.MovieBuzzIndex;
import com.moviebuzz.database.elasticsearch.models.EsTheatreMapping;
import com.moviebuzz.database.elasticsearch.service.ElasticsearchService;
import info.archinnov.achilles.generated.manager.TheatreEntity_Manager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheatreService
{

    @Autowired
    private TheatreEntity_Manager theatreEntityManager;

    @Autowired
    private ElasticsearchService elasticsearchService;

    public TheatreEntity getTheater(UUID uuid)
    {
        return theatreEntityManager.crud().findById(uuid).get();
    }

    public RestStatus addTheater(TheatreEntity theatreEntity)
            throws InterruptedException, ExecutionException, JsonProcessingException
    {
        theatreEntity.setTheaterUUID();
        theatreEntityManager.crud().insert(theatreEntity).execute();
        EsTheatreMapping theatreMapping = new EsTheatreMapping();
        BeanUtils.copyProperties(theatreEntity, theatreMapping);
        return elasticsearchService.indexDoc(theatreEntity.getUuid().toString(), theatreEntity,
                EsIndexMapping.getIndex(MovieBuzzIndex.THEATERS));
    }

    public List<EsTheatreMapping> getAllTheaters(Integer from, Integer size)
    {
        return elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.THEATERS),
                null, null, null,
                from, size, null, null, EsTheatreMapping.class);
    }

    public List<EsTheatreMapping> getTheatersNearUserByCityAndMovie(String city, UUID movieUuid,
                                                                    GeoPoint location,
                                                                    Integer from, Integer size)
    {
        Map<String, Object> matchMap = new HashMap<>();
        if(Objects.nonNull(city))
        {
            matchMap.put("city", city);
        }
        if(Objects.nonNull(movieUuid))
        {
            matchMap.put("activeMovies.uuid", movieUuid.toString());
        }
        return elasticsearchService.getAllDocs(EsIndexMapping.getIndex(MovieBuzzIndex.THEATERS),
                matchMap, null, location,
                from, size, null, null, EsTheatreMapping.class);
    }

}
