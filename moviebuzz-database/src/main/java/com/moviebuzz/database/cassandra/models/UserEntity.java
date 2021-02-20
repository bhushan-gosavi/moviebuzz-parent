package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  All the user details will be stored in cassandra moviebuzz.users table
 *  With PartitionKey as User UUID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "users")
public class UserEntity
{
    @PartitionKey(1)
    @Column
    private UUID uuid;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String email;

}
