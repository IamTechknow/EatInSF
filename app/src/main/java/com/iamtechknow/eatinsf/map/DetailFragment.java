package com.iamtechknow.eatinsf.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iamtechknow.eatinsf.R;
import com.iamtechknow.eatinsf.places.Restaurant;
import com.iamtechknow.eatinsf.places.RestaurantDetail;

import java.util.Locale;

/**
 * Bottom sheet dialog for displaying detailed restaurant information.
 * The default behavior for the bottom sheet is to dismiss itself when swiped to the screen's bottom
 */
public class DetailFragment extends BottomSheetDialogFragment {
    /**
     * Setup the bottom sheet behaviour and create the view for the bottom sheet
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle args = getArguments();
        Restaurant rest = args.getParcelable(MainActivity.REST_EXTRA);
        RestaurantDetail details = args.getParcelable(MainActivity.DETAIL_EXTRA);

        TextView name = root.findViewById(R.id.name), address = root.findViewById(R.id.address),
                phone_number = root.findViewById(R.id.phone_number), url = root.findViewById(R.id.maps_link),
                price = root.findViewById(R.id.price_range), rating = root.findViewById(R.id.rating);

        //Acquire text views and fill them with useful information
        if(rest != null && details != null) {
            address.setText(details.getAddress());
            phone_number.setText(details.getPhoneNumber());
            price.setText(getPriceDescription(details.getPriceLevel()));
            rating.setText(details.getRating() != 0.0F ? getString(R.string.rating_fmt, details.getRating()) : getString(R.string.no_rating));

            //Set the Google Maps URL as a Link, which must be shown
            url.setText(Html.fromHtml(urlAsHtml(details.getUrl())));
            url.setMovementMethod(LinkMovementMethod.getInstance());

            //If website present, add as hyperlink to the name
            name.setText(details.getWebsite() != null ?
                Html.fromHtml(addWebsiteToName(rest.getName(), details.getWebsite())) : rest.getName());
            if(details.getWebsite() != null)
                name.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return root;
    }

    //Get the price description, in the form of dollar signs.
    private String getPriceDescription(RestaurantDetail.PriceLevel price) {
        String priceStr;
        switch(price) {
            case INEXPENSIVE:
                priceStr = "$";
                break;
            case MODERATE:
                priceStr = "$$";
                break;
            case EXPENSIVE:
                priceStr = "$$$";
                break;
            case VERY_EXPENSIVE:
                priceStr = "$$$$";
                break;
            default:
                priceStr = "Unavailable";
        }

        return String.format(Locale.US, "Price Range: %s", priceStr);
    }

    //Turn the URL into a hyperlink with text
    private String urlAsHtml(String url) {
        return String.format(Locale.US, "<a href=\"%s\">View in Google Maps</a>", url);
    }

    private String addWebsiteToName(String name, String url) {
        return String.format(Locale.US, "<a href=\"%s\">%s</a>", url, name);
    }
}
