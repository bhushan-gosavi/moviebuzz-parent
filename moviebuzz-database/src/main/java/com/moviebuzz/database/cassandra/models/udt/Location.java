package com.moviebuzz.database.cassandra.models.udt;


import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.UDT;
import lombok.Data;

@Data
@UDT(keyspace = "moviebuzz", name = "location_udt")
public class Location
{
    @Column
    Double latitude;

    @Column
    Double longitude;
}
