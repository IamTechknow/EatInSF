package com.iamtechknow.eatinsf.places;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API Client to access events and categories from Google Places Web API. This client is Async,
 * data comes from callbacks after they are loaded in the background.
 */
public class PlacesClient {
    public interface LoadCallback {
        void onEventsLoaded(List<Restaurant> data);

        void onDetailLoaded(Restaurant r, RestaurantDetail detail);
    }

    private static final String BASE = "https://maps.googleapis.com";

    private final String apiKey;

    private PlacesAPI api;

    private LoadCallback callback;

    //Initialize the Retrofit API
    public PlacesClient(String key) {
        apiKey = key;

        //Create two type adapters for both endpoints being used
        Gson gson = new GsonBuilder().registerTypeAdapter(RestaurantList.class, new PlacesDeserializer())
                        .registerTypeAdapter(RestaurantDetail.class, new DetailDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        api = retrofit.create(PlacesAPI.class);
    }

    /**
     * Get a list of restaurants based on the center location and radius.
     */
    public Disposable getRestaurants(LatLng center, int radius) {
        return api.getNearbyPlaces(apiKey, convertCoordinate(center), radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listObj -> {
                if(callback != null)
                    callback.onEventsLoaded(listObj.list);
            });
    }

    /**
     * For a given location, query the Places API to get more info about it.
     */
    public Disposable getRestaurantDetail(Restaurant r) {
        return api.getDetails(apiKey, r.getPlacesId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(detail -> {
                if(callback != null)
                    callback.onDetailLoaded(r, detail);
            });
    }

    public void setCallback(LoadCallback callback) {
        this.callback = callback;
    }

    public void removeCallback() {
        callback = null;
    }

    private String convertCoordinate(LatLng coord) {
        return String.format(Locale.US,"%f,%f", coord.latitude, coord.longitude);
    }
}
