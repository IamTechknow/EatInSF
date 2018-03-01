package com.iamtechknow.eatinsf.map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.SupportMapFragment;
import com.iamtechknow.eatinsf.R;
import com.iamtechknow.eatinsf.places.PlacesClient;
import com.iamtechknow.eatinsf.places.Restaurant;
import com.iamtechknow.eatinsf.places.RestaurantDetail;

/**
 * Implementation of the Map View and main entry point into the application.
 */
public class MainActivity extends FragmentActivity implements MapContract.View {

    //Presenter
    private MapContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MapPresenterImpl(this, new MapInteractorImpl(), new PlacesClient(getAPIKey()));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(presenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void presentDetailScreen(Restaurant rest, RestaurantDetail restDetail) {
        //TODO: implement bottom frag
    }

    private String getAPIKey() {
        return getString(R.string.google_places_key);
    }
}
