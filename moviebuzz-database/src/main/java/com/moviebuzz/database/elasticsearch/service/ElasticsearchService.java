package com.moviebuzz.database.elasticsearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.moviebuzz.database.elasticsearch.index.EsIndex;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElasticsearchService
{
    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

    public RestStatus indexDoc(String docId, Object document, EsIndex esIndex)
        throws JsonProcessingException, ExecutionException, InterruptedException
    {
        IndexRequest indexRequest = new IndexRequest(esIndex.getIndex(), esIndex.getType(), docId);
        indexRequest.source(new ObjectMapper().writeValueAsString(document), XContentType.JSON);
        IndexResponse response = client.index(indexRequest).get();

        if(response.status().equals(RestStatus.CREATED))
        {
            log.info("Document indexed successfully. index: {}, docId: {}", esIndex, docId);
        }
        else
        {
            log.error("Document indexing failed. index: {}, docId: {}, status: {}",
                esIndex, docId, response.status());
        }

        return response.status();
    }

    public <T> List<T> getAllDocs(EsIndex esIndex,
                                  Map<String, Object> matchMap,
                                  Map<String, List<Object>> termsMap,
                                  GeoPoint location,
                                  Integer from,
                                  Integer size, String sortBy, SortOrder sortOrder, Class<T> classType)
    {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if(Objects.isNull(matchMap) && Objects.isNull(termsMap) && Objects.isNull(location))
        {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        else
        {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

            if(Objects.nonNull(matchMap))
            {
                for(String match: matchMap.keySet())
                {
                    queryBuilder = queryBuilder
                            .must(QueryBuilders.matchQuery(match, matchMap.get(match)));
                }
            }

            if(Objects.nonNull(termsMap))
            {
                for(String term: termsMap.keySet())
                {
                    queryBuilder = queryBuilder
                            .must(QueryBuilders.termsQuery(term, termsMap.get(term)));
                }
            }

            if (Objects.nonNull(location)) {
                queryBuilder = queryBuilder
                        .filter(QueryBuilders.geoDistanceQuery("location")
                                .point(location).distance(100d, DistanceUnit.KILOMETERS));
            }

            searchSourceBuilder.query(queryBuilder);
        }



        if(Objects.nonNull(sortBy))
        {
            searchSourceBuilder.sort(sortBy, sortOrder);
        }

        if(Objects.nonNull(from) && Objects.nonNull(size))
        {
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);
        }

        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(esIndex.getIndex());

        SearchResponse searchResponse = client.search(searchRequest).actionGet();
        return Arrays.stream(searchResponse.getHits().getHits()).map(searchHit -> {
            return gson.fromJson(searchHit.getSourceAsString(), classType);
        }).collect(Collectors.toList());

    }


}
