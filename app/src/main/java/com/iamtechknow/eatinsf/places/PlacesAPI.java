package com.iamtechknow.eatinsf.places;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * High-level API based on Retrofit to obtain data about restaurants
 */
public interface PlacesAPI {
    //Search restaurants by location (up to 20 results per search)
    @GET("/maps/api/place/nearbysearch/json?type=restaurant")
    Observable<RestaurantList> getNearbyPlaces(@Query("key") String apiKey, @Query("location") String loc, @Query("radius") int radius);

    //Get up to next 20 results for a search query
    @GET("/maps/api/place/nearbysearch/json?")
    Observable<RestaurantList> getNextPage(@Query("key") String apiKey, @Query("pagetoken") String token);

    //Get more detailed information about a restaurant.
    @GET("/maps/api/place/details/json?type=restaurant")
    Observable<RestaurantDetail> getDetails(@Query("key") String apiKey, @Query("placeid") String id);
}
