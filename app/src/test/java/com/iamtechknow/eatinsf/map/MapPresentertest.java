package com.iamtechknow.eatinsf.map;

import com.google.android.gms.maps.model.LatLng;
import com.iamtechknow.eatinsf.places.PlacesClient;
import com.iamtechknow.eatinsf.places.Restaurant;
import com.iamtechknow.eatinsf.places.RestaurantDetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests of the callback implementations of the Map Presenter.
 */
public class MapPresentertest {
    @Mock
    private MapContract.View view;

    @Mock
    private PlacesClient client;

    @Mock
    private MapInteractor mapInteractor;

    private MapPresenterImpl presenter;

    //Fake data
    private Restaurant sample = new Restaurant("Sample", "id1", new LatLng(37.751196, -122.436377)),
    sample2 = new Restaurant("Sample2", "id2", CENTER_OF_SF),
    sample3 = new Restaurant("Sample3", "id3", new LatLng(37.751000, -122.436000));

    private static final LatLng CENTER_OF_SF = new LatLng(37.751196, -122.436377);

    @Before
    public void setup() {
        //Setup presenter
        MockitoAnnotations.initMocks(this);
        presenter = new MapPresenterImpl(view, mapInteractor, client);
        client.setCallback(presenter);
        mapInteractor.setCallback(presenter);

        //Mock getting the GoogleMap. This may break upon changes to the GMaps library
        presenter.onMapReady(null);
    }

    @Test
    public void testLoadData() {
        //Setup, load events by mocking a query
        List<Restaurant> list = Arrays.asList(sample, sample2, sample3);
        doAnswer(invocation -> {
            presenter.onDataLoaded(list);
            return null;
        }).when(client).getRestaurants(CENTER_OF_SF, 1000);
        when(mapInteractor.calculateRadius()).thenReturn(1000);

        //Act
        presenter.onCameraIdle(CENTER_OF_SF);

        //Verify
        for(Restaurant r : list)
            verify(mapInteractor).placeMarker(r.getCoords());
    }

    @Test
    public void testMarkerPress() {
        //Setup, mock a presenter callback
        RestaurantDetail detail = new RestaurantDetail("id1", 2, "https://maps.google.com", "(415) 333-3333", "Address", "Site",  5.0F);
        doAnswer(invocation -> {
            presenter.onDetailLoaded(sample, detail);
            return null;
        }).when(client).getRestaurantDetail(sample);

        //Act
        presenter.onDataLoaded(Collections.singletonList(sample));
        presenter.onMarkerSelected(sample.getCoords());

        //Verify
        verify(view).presentDetailScreen(sample, detail);
    }

    @Test
    public void testBadDetailQuery() {
        //Setup
        doAnswer(invocation -> {
            presenter.onDetailLoaded(sample, RestaurantDetail.DUMMY);
            return null;
        }).when(client).getRestaurantDetail(sample);

        //Act
        presenter.onDataLoaded(Collections.singletonList(sample));
        presenter.onMarkerSelected(sample.getCoords());

        //Verify
        verify(view, times(0)).presentDetailScreen(any(), any());
        verify(view).warnUserBadQuery();
    }
}
