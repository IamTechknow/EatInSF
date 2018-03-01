package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.iamtechknow.eatinsf.places.Restaurant;
import com.iamtechknow.eatinsf.places.RestaurantDetail;

public interface MapContract {
    /**
     * Contract for the view, exposes all methods needed by the presenter.
     */
    interface View {
        void presentDetailScreen(Restaurant rest, RestaurantDetail restDetail);
    }

    /**
     * Presenter portion of the Map contract, exposes all methods needed by the map view.
     */
    interface Presenter extends OnMapReadyCallback {
        void detachView();
    }
}
