package com.iamtechknow.eatinsf.places;

import com.google.android.gms.maps.model.LatLng;

/**
 * Model representation for a Restaurant. More detailed information is separated to another class.
 */
public class Restaurant {
    private final String name;
    private final String placesid;
    private final LatLng coords;



    public Restaurant(String _name, String id, LatLng coord) {
        name = _name;
        placesid = id;
        coords = coord;
    }

    public String getName() {
        return name;
    }

    public String getPlacesId() {
        return placesid;
    }

    public LatLng getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Checks if two objects are equal to each other.
     * Two Restaurant objects are equal if they have the same Places ID
     * @param obj Object to compare
     * @return Whether or not this object refers to the same restaurant as the other
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj instanceof Restaurant) {
            Restaurant that = (Restaurant) obj;
            return placesid.equals(that.placesid);
        }

        return false;
    }
}
