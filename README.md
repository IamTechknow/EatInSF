# Eat in SF Android Application

## Requirements:

* Use Google Maps API and Places API Web Service
* Display restaurants in San Francisco onto the map as markers
* Use the specified Google Places API key
* When user taps on a Marker, App should display another screen showing details of the selected restaurant

## Assumptions to make:
* What parts of the APIs will the app be using? How should I get such info?
* How to display details of a restaurant? We may use another activity, a marker popup, a dialog or a bottom sheet.
* What information should the app cache? What information should the app store in a persistent storage?
* How many Markers should the app show on the screen, based on GMaps settings?
* Marker API: We can use GoogleMap.OnMarkerClickListener to get the Marker that is clicked to show a bottom sheet

## Places API:
* How to get data on restaurants for a given location? Use Place Searches (Nearby Search) with parameters: api key, restaurant type, location, radius in meters from location
* How to get specific data on a restaurant? Use Place Details with a placeid. Can be done with GeoDataClient

## Minimum Specification:
* The application will use MVP, so it consists of one activity as a view with a presenter
* The presenter will contain an Interactor for GMaps and Places
* The application will get restaurant data in two ways, either remotely from Places API, or by a local Sqlite3 database to save time and API calls
* Obtain Places API data by using Retrofit and a GsonDeserializer adapter to deserialize the data into our Restaurant model
* Use the restaurant data to display Markers on the map
* Decide what markers to display and what to hide based on Map callbacks
* Display details by using a callback for a selected marker to present a BottomSheetFragment, and use cached data to access details

## Post min-spec tasks:
* Unit tests with Mockito
* Handle configuration changes
