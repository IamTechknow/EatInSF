package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.iamtechknow.eatinsf.places.PlacesClient;
import com.iamtechknow.eatinsf.places.Restaurant;
import com.iamtechknow.eatinsf.places.RestaurantDetail;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Map presenter implementation used by the view to decouple application specific data.
 */
public class MapPresenterImpl implements MapContract.Presenter,
                MapInteractor.MapCallbacks, PlacesClient.LoadCallback {

    //Map Interactor
    private MapInteractor map;

    //Map View
    private MapContract.View view;

    //Places API Client
    private PlacesClient client;

    //Hashmaps for quickly accessing restaurant data
    private HashMap<LatLng, Restaurant> restMap;
    private HashMap<String, RestaurantDetail> detailMap;

    //Subscriptions to API queries, can be used to cancel if needed
    private Disposable listQuerySub, detailSub;

    public MapPresenterImpl(MapContract.View v, MapInteractor interactor, PlacesClient places) {
        view = v;
        map = interactor;
        map.setCallback(this);
        client = places;
        client.setCallback(this);

        restMap = new HashMap<>();
        detailMap = new HashMap<>();
    }

    //Clean up to avoid memory leaks
    @Override
    public void detachView() {
        view = null;
        map.removeCallback();
        client.removeCallback();

        if(listQuerySub != null) {
            listQuerySub.dispose();
            listQuerySub = null;
        }

        if(detailSub != null) {
            detailSub.dispose();
            detailSub = null;
        }
    }


    //No internet - don't try to make API queries and warn the user
    @Override
    public void reportNoInternet() {
        client.removeCallback();
        map.removeCallback();
        view.warnUserNoInternet();
    }

    @Override
    public void reportInternet() {
        client.setCallback(this);
        map.setCallback(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.onMapReady(googleMap);
    }

    //Add data to restaurant HashMap, populate screen with Markers
    @Override
    public void onDataLoaded(List<Restaurant> data) {
        //Check if the Marker already exists
        for(Restaurant r : data) {
            LatLng r_coord = r.getCoords();
            if(!restMap.containsKey(r_coord)) {
                restMap.put(r_coord, r);
                map.placeMarker(r_coord);
            }
        }
    }

    //Check for successful query, add data to detail HashMap, present screen
    //An unsuccessful query may happen if markers loaded right before quota was hit
    @Override
    public void onDetailLoaded(Restaurant r, RestaurantDetail detail) {
        if(!detail.equals(RestaurantDetail.DUMMY)) {
            detailMap.put(r.getPlacesId(), detail);
            view.presentDetailScreen(r, detail);
        } else
            view.warnUserBadQuery();
    }

    //Request for more restaurants
    @Override
    public void onCameraIdle(LatLng center) {
        listQuerySub = client.getRestaurants(center, map.calculateRadius());
    }

    /**
     * Query API for restaurant detail. This is an async request
     * If the details are already then just show the screen!
     * @param coord The coordinate of the selected marker representing a restaurant
     */
    @Override
    public void onMarkerSelected(LatLng coord) {
        Restaurant r = restMap.get(coord);

        if(detailMap.containsKey(r.getPlacesId()))
            view.presentDetailScreen(r, detailMap.get(r.getPlacesId()));
        else
            detailSub = client.getRestaurantDetail(restMap.get(coord));
    }
}
