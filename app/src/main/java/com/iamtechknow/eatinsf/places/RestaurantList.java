package com.iamtechknow.eatinsf.places;

import java.util.List;

/**
 * Container class used for JSON serialization
 */
class RestaurantList {
    public List<Restaurant> list;

    public RestaurantList(List<Restaurant> list) {
        this.list = list;
    }
}
