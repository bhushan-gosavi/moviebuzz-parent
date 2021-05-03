package com.moviebuzz.database.cassandra.models;

import info.archinnov.achilles.annotations.Column;
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
 *  All the user details will be stored in cassandra moviebuzz.users table
 *  With PartitionKey as User UUID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "moviebuzz", table = "users")
public class UserEntity
{
    @PartitionKey(1)
    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    // password will be stored in encoded form
    private String encodedPassword;

    @Column
    private Long mobileNumber;

    @Column
    private Boolean isAccountNonExpired;

    @Column
    private Boolean isAccountNonLocked;

    @Column
    private Boolean isCredentialsNonExpired;

    @Column
    private Boolean isEnabled;

    @Column
    private Set<@Frozen String> roles;

}
