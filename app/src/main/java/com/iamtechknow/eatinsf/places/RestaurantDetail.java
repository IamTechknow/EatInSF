package com.iamtechknow.eatinsf.places;

/**
 * Model representation for more detailed Restaurant information that would be displayed to the user.
 */
public class RestaurantDetail {
    enum PriceLevel {FREE, INEXPENSIVE, MODERATE, EXPENSIVE, VERY_EXPENSIVE}

    private final String placesid;
    private final PriceLevel priceLevel;
    private final String url;
    private final String phoneNumber;
    private final String address;
    private final float rating;

    public RestaurantDetail(String id, int lvl, String _url, String phone, String addr, float _rating) {
        placesid = id;
        priceLevel = getPriceLevel(lvl);
        url = _url;
        phoneNumber = phone;
        address = addr;
        rating = _rating;
    }

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
                return PriceLevel.FREE;
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
}
