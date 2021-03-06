package com.moviebuzz.database.service;

import com.moviebuzz.database.cassandra.models.UserBookingEntity;
import info.archinnov.achilles.generated.manager.UserBookingEntity_Manager;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService
{

    @Autowired
    private UserBookingEntity_Manager bookingsManager;

    public void addBooking(UserBookingEntity userBooking)
    {
        bookingsManager.crud().insert(userBooking);
    }

    public List<UserBookingEntity> getUserBookings(UUID userId)
    {
        return bookingsManager.dsl().select().allColumns_FromBaseTable()
            .where().userId().Eq(userId)
            .orderByBookingIdDescending()
            .getList();
    }



}
