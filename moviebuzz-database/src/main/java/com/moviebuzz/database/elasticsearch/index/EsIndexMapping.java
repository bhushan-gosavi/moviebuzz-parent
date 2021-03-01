package com.moviebuzz.database.elasticsearch.index;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class EsIndexMapping
{
    public enum MovieBuzzIndex
    {
        MOVIES,
        THEATERS
    }

    private static Map<MovieBuzzIndex, EsIndex> indexMap = ImmutableMap.<MovieBuzzIndex, EsIndex>builder()
            .put(MovieBuzzIndex.MOVIES, EsIndex.builder().index("moviebuzz_movies").type("movies").build())
            .put(MovieBuzzIndex.THEATERS, EsIndex.builder().index("moviebuzz_theaters").type("theaters").build())
            .build();


    public static EsIndex getIndex(MovieBuzzIndex index)
    {
        return indexMap.get(index);
    }

}
