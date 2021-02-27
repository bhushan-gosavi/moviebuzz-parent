package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.ClusteringColumn;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the user-ratings will be stored in cassandra moviebuzz.user_ratings table
 *  With PartitionKey as User UUID and movieUuid as clustering column
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "users_reviews")
public class UserReviewsEntity
{
    @PartitionKey
    @Column
    private UUID userUuid;

    @ClusteringColumn
    @Column
    private UUID movieUuid;

    @Column
    private String review;

    @Column
    private Float rating;

    @Column
    private Date date;
}
