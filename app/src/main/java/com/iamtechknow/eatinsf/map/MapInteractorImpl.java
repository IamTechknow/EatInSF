package com.iamtechknow.eatinsf.map;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Interactor implementation used by the Map Presenter to decouple GMaps from the presenter.
 */
public class MapInteractorImpl implements MapInteractor {
    //Used to center the map to San Francisco. Uses Diamond Street and 24th Street as the location
    private static final LatLng CENTER_OF_SF = new LatLng(37.751196, -122.436377);

    //Define an initial zoom level and a range for which restaurants can load. This helps prevent extraneous API calls
    private static final float DEFAULT_ZOOM = 12.5f, MIN_LOADING_ZOOM = 13.5f, MAX_LOADING_ZOOM = 17.5f;

    //The maximum radius for loading restaurants. Capping the radius helps keeps markers inside the screen.
    //Setting a move threshold helps prevent loading due to accidental swipes.
    private static final int MAX_RADIUS_M = 2000, MOVE_THRESHOLD = 150;

    //Gmaps instance
    private GoogleMap gMaps;

    private MapCallbacks callback;

    //Keep track of where the map center was to calculate moved distance
    private LatLng previousCenter;

    //Shows most or all of SF (depending on the device's screen density)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMaps = googleMap;
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_OF_SF, DEFAULT_ZOOM));
        gMaps.setOnMarkerClickListener(this);
        gMaps.setOnCameraIdleListener(this);

        previousCenter = CENTER_OF_SF;
    }

    /**
     * Obtain the visible region and calculate the distance from the center of the screen
     * to the middle left corner of the screen.
     * Attribution: https://stackoverflow.com/questions/20422701/retrieve-distance-from-visible-part-of-google-map
     * @return A radius as input for the Places Search query
     */
    @Override
    public int calculateRadius() {
        LatLngBounds bounds = gMaps.getProjection().getVisibleRegion().latLngBounds;
        LatLng center = gMaps.getCameraPosition().target;

        Location c = new Location("center"), mid = new Location("mid");
        c.setLatitude(center.latitude);
        c.setLongitude(center.longitude);
        mid.setLatitude(center.latitude);
        mid.setLongitude(bounds.southwest.longitude);

        return (int) Math.max(MAX_RADIUS_M, mid.distanceTo(c));
    }

    @Override
    public void placeMarker(LatLng coord) {
        MarkerOptions marker = new MarkerOptions().position(coord);
        gMaps.addMarker(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(callback != null)
            callback.onMarkerSelected(marker.getPosition());
        return callback != null;
    }

    //The maps has stopped moving. Decide whether or not restaurants should be queried
    //based on zoom level.
    @Override
    public void onCameraIdle() {
        if(callback != null && shouldLoadLocations(gMaps.getCameraPosition().target))
            callback.onCameraIdle(gMaps.getCameraPosition().target);

        //Update the map center
        previousCenter = gMaps.getCameraPosition().target;
    }

    public void setCallback(MapInteractor.MapCallbacks callback) {
        this.callback = callback;
    }

    public void removeCallback() {
        callback = null;
    }

    //Is zoom level adequate, and did the user move the map enough?
    //Good idea, but the distance calculation needs to take into account of zoom for it to really work.
    private boolean shouldLoadLocations(LatLng newCoords) {
        float zoom = gMaps.getCameraPosition().zoom;
        if(!(zoom >= MIN_LOADING_ZOOM && zoom <= MAX_LOADING_ZOOM))
            return false;

        //Calculate the distance between the old and new map centers
        Location oldCenter = new Location("oldCenter"), newCenter = new Location("newCenter");
        oldCenter.setLatitude(previousCenter.latitude);
        oldCenter.setLongitude(previousCenter.longitude);
        newCenter.setLatitude(newCoords.latitude);
        newCenter.setLongitude(newCoords.longitude);

        return oldCenter.distanceTo(newCenter) > MOVE_THRESHOLD;
    }
}
