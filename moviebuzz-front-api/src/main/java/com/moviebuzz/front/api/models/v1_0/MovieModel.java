package com.moviebuzz.front.api.models.v1_0;

import com.moviebuzz.database.cassandra.models.udt.Person;
import info.archinnov.achilles.annotations.Frozen;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class MovieModel
{
    private UUID uuid;
    private String name;
    private String description;
    private Date released;
    private Set<PersonModel> actors;
    private Set<PersonModel> crew;
    private Set<String> genres;
    private Set<String> languages;
    private String imageUrl;
    private String certificate;
}
