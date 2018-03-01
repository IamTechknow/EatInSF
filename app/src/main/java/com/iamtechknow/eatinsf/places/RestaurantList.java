package com.iamtechknow.eatinsf.places;

import java.util.List;

/**
 * Container class used for JSON serialization. Contains the result of a Places API search,
 * which may contain a token for more results.
 */
class RestaurantList {
    public final List<Restaurant> list;

    public final String page_token;

    public RestaurantList(List<Restaurant> list, String token) {
        this.list = list;
        page_token = token;
    }
}
