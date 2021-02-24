package com.moviebuzz.database.elasticsearch.index;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EsIndex
{
    private String index;

    private String type;
}
