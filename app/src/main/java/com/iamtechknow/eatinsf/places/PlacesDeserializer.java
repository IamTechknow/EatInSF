package com.iamtechknow.eatinsf.places;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Parses the JSON received from the Place Search API call, and returns minimal restaurant data to be displayed as markers.
 * Note that a container object must be returned because the type adapter cannot support generic types.
 */
class PlacesDeserializer implements JsonDeserializer<RestaurantList> {
    @Override
    public RestaurantList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Restaurant> result = new ArrayList<>();
        JsonObject root = json.getAsJsonObject();
        String status = root.get("status").getAsString();
        String token = root.has("next_page_token") ? root.get("next_page_token").getAsString() : null;

        //Results present, otherwise return empty list
        if(status.equals("OK")) {
            JsonArray array = json.getAsJsonObject().getAsJsonArray("results");

            for(JsonElement e : array) {
                JsonObject theObj = e.getAsJsonObject(), loc = theObj.getAsJsonObject("geometry").getAsJsonObject("location");
                LatLng coord = new LatLng(loc.get("lat").getAsFloat(), loc.get("lng").getAsFloat());
                String id = theObj.get("place_id").getAsString(), name = theObj.get("name").getAsString();

                result.add(new Restaurant(name, id, coord));
            }
        }

        return new RestaurantList(result, token);
    }
}
