package com.iamtechknow.eatinsf.places;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Handles deserialization of data from a single Places detail API call.
 */
public class DetailDeserializer implements JsonDeserializer<RestaurantDetail> {
    @Override
    public RestaurantDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String status = json.getAsJsonObject().get("status").getAsString();
        RestaurantDetail detail = null;

        //Result present, otherwise return null
        if(status.equals("OK")) {
            JsonObject theObj = json.getAsJsonObject().getAsJsonObject("result");

            String id = theObj.get("place_id").getAsString(), addr = theObj.get("formatted_address").getAsString(),
                    phone = theObj.get("formatted_phone_number").getAsString(), url = theObj.get("url").getAsString();
            int level = theObj.get("price_level").getAsInt();
            float rating = theObj.get("rating").getAsFloat();

            detail = new RestaurantDetail(id, level, url, phone, addr, rating);
        }

        return detail;
    }
}
