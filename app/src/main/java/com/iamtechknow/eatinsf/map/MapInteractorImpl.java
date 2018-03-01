package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Interactor implementation used by the Map Presenter to decouple GMaps from the presenter.
 */
public class MapInteractorImpl implements MapInteractor {
    //Used to center the map to San Francisco. Uses Diamond Street and 24th Street as the location
    private static final LatLng CENTER_OF_SF = new LatLng(37.751196, -122.436377);
    private static final int DEFAULT_ZOOM = 12;

    //Gmaps instance
    private GoogleMap gMaps;

    private MapCallbacks callback;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMaps = googleMap;
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_OF_SF, DEFAULT_ZOOM));
        gMaps.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onCameraIdle() {

    }

    public void setCallback(MapInteractor.MapCallbacks callback) {
        this.callback = callback;
    }

    public void removeCallback() {
        callback = null;
    }
}
