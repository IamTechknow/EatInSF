package com.iamtechknow.eatinsf.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
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
    static final String REST_EXTRA = "rest", DETAIL_EXTRA = "detail";

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

    //Check for internet when app starts or is resumed. Also start a broadcast receiver
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectReceiver, filter);

        if(!isOnline())
            presenter.reportNoInternet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectReceiver);
        presenter.detachView();
    }

    @Override
    public void presentDetailScreen(Restaurant rest, RestaurantDetail restDetail) {
        BottomSheetDialogFragment frag = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(REST_EXTRA, rest);
        args.putParcelable(DETAIL_EXTRA, restDetail);

        frag.setArguments(args);
        frag.show(getSupportFragmentManager(), frag.getTag());
    }

    @Override
    public void warnUserNoInternet() {
        Snackbar.make(findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Helper method to determine whether or not there is internet access
     * @return Whether or not there is internet access
     */
    private boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * Used to indicate internet connectivity is available to load Worldview and GIBS data.
     * Not used when internet is already available or data already obtained
     */
    private final BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && isOnline())
                presenter.reportInternet();
            else
                presenter.reportNoInternet();
        }
    };

    private String getAPIKey() {
        return getString(R.string.google_places_key);
    }
}
