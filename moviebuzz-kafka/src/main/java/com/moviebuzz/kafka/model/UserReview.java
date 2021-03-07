package com.moviebuzz.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReview
{
    private UUID userUuid;
    private UUID movieUuid;
    private String review;
    private Long rating;
    private Date date;
}
