package com.moviebuzz.front.api.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moviebuzz.front.api.models.v1_0.BookingModel;
import com.moviebuzz.front.api.models.v1_0.MovieModel;
import com.moviebuzz.front.api.models.v1_0.TheaterModel;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class APIService
{
    @Value("${moviebuzz.api.url}")
    private String moviebuzzApiUrl;

    @Autowired
    private Gson gson;

    OkHttpClient client = new OkHttpClient();


    public List<TheaterModel> getTheatersNearUserByCityAndMovie(String cityName, UUID movieId, Double lat,
        Double lon, Integer from, Integer size) throws IOException
    {

        HttpUrl.Builder urlBuilder
            = Objects.requireNonNull(HttpUrl.parse(
            moviebuzzApiUrl + "/moviebuzz/v1.0/theatres/city/" + cityName + "/movies/" + movieId))
            .newBuilder();
        if(Objects.nonNull(lat) && Objects.nonNull(lon))
        {
            urlBuilder.addQueryParameter("lat", lat.toString());
            urlBuilder.addQueryParameter("lon", lon.toString());
        }

        if(Objects.nonNull(from))
        {
            urlBuilder.addQueryParameter("from", from.toString());
        }

        if(Objects.nonNull(size))
        {
            urlBuilder.addQueryParameter("size", size.toString());
        }

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
            .url(url)
            .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        return this.gson.fromJson(
            Objects.requireNonNull(response.body()).charStream(), new TypeToken<List<TheaterModel>>() {
            }.getType());

    }


    public MovieModel getMovie(UUID movieId) throws IOException
    {

        HttpUrl.Builder urlBuilder
            = Objects.requireNonNull(HttpUrl.parse(
            moviebuzzApiUrl + "/moviebuzz/v1.0/movies/" + movieId))
            .newBuilder();

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
            .url(url)
            .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        return this.gson.fromJson(
            Objects.requireNonNull(response.body()).charStream(), new TypeToken<MovieModel>() {
            }.getType());

    }

    public List<BookingModel> getBookings(String username) throws IOException
    {

        HttpUrl.Builder urlBuilder
            = Objects.requireNonNull(HttpUrl.parse(
            moviebuzzApiUrl + "/moviebuzz/v1.0/bookings/" + username))
            .newBuilder();

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
            .url(url)
            .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        return this.gson.fromJson(
            Objects.requireNonNull(response.body()).charStream(), new TypeToken<List<BookingModel>>() {
            }.getType());

    }
}
