package com.moviebuzz.database.cassandra.models;

import com.moviebuzz.database.cassandra.enums.Language;
import com.moviebuzz.database.cassandra.enums.MovieGenres;
import com.moviebuzz.database.cassandra.models.udt.Person;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Enumerated;
import info.archinnov.achilles.annotations.Frozen;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the movies details will be stored in cassandra moviebuzz.movies table
 *  With PartitionKey as Movie UUID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "movies")
public class MovieEntity
{
    @PartitionKey(1)
    @Column
    private UUID uuid;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Date released;

    @Column
    private Boolean isBookingActive;

    @Column
    private Set<@Frozen Person> actors;

    @Column
    private Set<@Frozen Person> crew;

    @Column
    private Set<@Enumerated MovieGenres> genres;

    @Column
    private Set<@Enumerated Language> languages;

    @Column
    private String imageUrl;

    @Column
    private String certificate;



    public static UUID generateUUID(String movieName, Date released)
    {
        return UUID.nameUUIDFromBytes((movieName + released.toString()).getBytes());
    }

    public void setMovieUUID()
    {
        uuid = generateUUID(name, released);
    }


}
