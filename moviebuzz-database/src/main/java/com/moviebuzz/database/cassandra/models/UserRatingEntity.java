package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the user-ratings will be stored in cassandra moviebuzz.user_ratings table
 *  With PartitionKey as User UUID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "users_ratings")
public class UserRatingEntity
{
    @PartitionKey(1)
    @Column
    private UUID userUuid;

    @Column
    private UUID movieUuid;

    @Column
    private Float rating;

}
