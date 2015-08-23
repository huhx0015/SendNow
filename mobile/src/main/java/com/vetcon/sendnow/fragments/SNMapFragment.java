package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vetcon.sendnow.R;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */

public class SNMapFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Activity currentActivity;

    // LAYOUT VARIABLES
    private static View view;

    // MAP VARIABLES
    double centerLatitude = 33.494310; // Sets the x-coordinates for Syria.
    double centerLongitude = 36.283822; // Sets the y-coordinates for Syria.
    private GoogleMap googleMap;
    private MapView mapView;

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        view = (ViewGroup) inflater.inflate(R.layout.sn_map_fragment_layout, container, false);
        ButterKnife.bind(this, view); // ButterKnife view injection initialization.

        setUpButtons(); // Sets up the button listeners for the fragment.
        setUpMap(savedInstanceState); // Sets up the Google Maps view.

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setUpButtons() {}

    /** GOOGLE MAP METHODS _____________________________________________________________________ **/

    private void setUpMap(Bundle savedInstanceState) {

        mapView = (MapView) view.findViewById(R.id.sn_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mapView.getMap();

        addDummyMarkers(); // Populate map with dummy data.

        // Sets the coordinates.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(centerLatitude, centerLongitude)).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void addDummyMarkers() {

        // Create markers
        MarkerOptions marker1 = setMarkerLocation("International Rescue Committee", centerLatitude, centerLongitude);
        MarkerOptions marker2 = setMarkerLocation("Doctors Without Borders", 33.527405, 36.290373);
        MarkerOptions marker3 = setMarkerLocation("ICRC", 33.515839, 36.321202);
        MarkerOptions marker4 = setMarkerLocation("InterAction", 33.533830, 36.242588);

        // Changing marker icon
        marker1 = setMarkerIcon(marker1);
        marker2 = setMarkerIcon(marker2);
        marker3 = setMarkerIcon(marker3);
        marker4 = setMarkerIcon(marker4);

        // adding marker
        googleMap.addMarker(marker1);
        googleMap.addMarker(marker2);
        googleMap.addMarker(marker3);
        googleMap.addMarker(marker4);
    }

    // Creates a new marker.
    private MarkerOptions setMarkerLocation(String location, double latitude, double longitude) {

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(location);

        return marker;
    }

    // Sets the marker icon properties.
    private MarkerOptions setMarkerIcon(MarkerOptions marker) {

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));

        return marker;
    }
}