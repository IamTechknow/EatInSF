package com.iamtechknow.eatinsf.places;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model representation for more detailed Restaurant information that would be displayed to the user.
 */
public class RestaurantDetail implements Parcelable {
    public enum PriceLevel {UNKNOWN, INEXPENSIVE, MODERATE, EXPENSIVE, VERY_EXPENSIVE}

    private final String placesid;
    private final PriceLevel priceLevel;
    private final String url;
    private final String phoneNumber;
    private final String address;
    private final String website;
    private final float rating;

    //Dummy object that effectively serves as null, because RxJava 2 doesn't allow null to be emitted
    public static final RestaurantDetail DUMMY = new RestaurantDetail("", 0, "", "", "", "", 1);

    public RestaurantDetail(String id, int lvl, String _url, String phone, String addr, String site, float _rating) {
        placesid = id;
        priceLevel = getPriceLevel(lvl);
        url = _url;
        phoneNumber = phone;
        address = addr;
        website = site;
        rating = _rating;
    }

    protected RestaurantDetail(Parcel in) {
        placesid = in.readString();
        priceLevel = getPriceLevel(in.readInt());
        url = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        website = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<RestaurantDetail> CREATOR = new Creator<RestaurantDetail>() {
        @Override
        public RestaurantDetail createFromParcel(Parcel in) {
            return new RestaurantDetail(in);
        }

        @Override
        public RestaurantDetail[] newArray(int size) {
            return new RestaurantDetail[size];
        }
    };

    public PriceLevel getPriceLevel() {
        return priceLevel;
    }

    public String getPlacesid() {
        return placesid;
    }

    public String getUrl() {
        return url;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public float getRating() {
        return rating;
    }

    private PriceLevel getPriceLevel(int lvl) {
        switch(lvl) {
            case 1:
                return PriceLevel.INEXPENSIVE;
            case 2:
                return PriceLevel.MODERATE;
            case 3:
                return PriceLevel.EXPENSIVE;
            case 4:
                return PriceLevel.VERY_EXPENSIVE;
            default:
                return PriceLevel.UNKNOWN;
        }
    }

    /**
     * Checks if two objects are equal to each other.
     * Two Restaurant objects are equal if they have the same Places ID
     * @param obj Object to compare
     * @return Whether or not this object refers to the restaurant detail as the other
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj instanceof RestaurantDetail) {
            RestaurantDetail that = (RestaurantDetail) obj;
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
        dest.writeString(placesid);
        dest.writeInt(priceLevel.ordinal());
        dest.writeString(url);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(website);
        dest.writeFloat(rating);
    }
}
