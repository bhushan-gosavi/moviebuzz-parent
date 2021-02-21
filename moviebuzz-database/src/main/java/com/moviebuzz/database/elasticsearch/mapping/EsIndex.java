package com.moviebuzz.database.elasticsearch.mapping;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EsIndex
{
    private String index;

    private String type;
}
