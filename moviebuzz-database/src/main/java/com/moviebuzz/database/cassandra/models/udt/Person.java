package com.moviebuzz.database.cassandra.models.udt;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.UDT;
import lombok.Data;

@Data
@UDT(keyspace = "moviebuzz", name = "person_udt")
public class Person {

    @Column("name")
    String name;

    @Column("role")
    String role;

    @Column("imageUrl")
    String imageUrl;

}
