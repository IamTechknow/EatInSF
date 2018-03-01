package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Interface for the GoogleMap, controls map interaction requested by the Presenter.
 */
public interface MapInteractor extends OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                                        GoogleMap.OnCameraIdleListener {

    //Callback for camera idling, implemented by Presenter
    interface MapCallbacks {
        void onCameraIdle(LatLng center);

        void onMarkerSelected(LatLng coord);
    }

    int calculateRadius();

    void placeMarker(LatLng coord);

    void setCallback(MapInteractor.MapCallbacks callback);

    void removeCallback();
}
