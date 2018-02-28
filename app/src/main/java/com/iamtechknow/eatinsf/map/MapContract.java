package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.OnMapReadyCallback;

public interface MapContract {
    /**
     * Contract for the view, exposes all methods needed by the presenter.
     */
    interface View {

    }

    /**
     * Presenter portion of the Map contract, exposes all methods needed by the map view.
     */
    interface Presenter extends OnMapReadyCallback {
        void detachView();
    }
}
