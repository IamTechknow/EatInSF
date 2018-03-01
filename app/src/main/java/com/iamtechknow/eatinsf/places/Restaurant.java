package com.iamtechknow.eatinsf.places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Model representation for a Restaurant. More detailed information is separated to another class.
 */
public class Restaurant implements Parcelable {
    private final String name;
    private final String placesid;
    private final LatLng coords;

    public Restaurant(String _name, String id, LatLng coord) {
        name = _name;
        placesid = id;
        coords = coord;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        placesid = in.readString();
        coords = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(placesid);
        dest.writeParcelable(coords, flags);
    }
}
