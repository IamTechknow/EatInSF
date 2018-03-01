package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Map presenter implementation used by the view to decouple application specific data.
 */
public class MapPresenterImpl implements MapContract.Presenter, MapInteractor.MapCallbacks {

    //Map Interactor
    private MapInteractor map;

    //Map View
    private MapContract.View view;

    public MapPresenterImpl(MapContract.View v, MapInteractor interactor) {
        view = v;
        map = interactor;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.onMapReady(googleMap);
    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onMarkerSelected(LatLng coord) {

    }
}
