package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.intent.SNIntent;

import java.util.Random;

import butterknife.Bind;
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

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_map_first_banner_button_1) ImageButton dwbBanner;
    @Bind(R.id.sn_map_first_banner_button_2) ImageButton iaBanner;
    @Bind(R.id.sn_map_first_banner_button_3) ImageButton icrcBanner;
    @Bind(R.id.sn_map_first_banner_button_4) ImageButton ircBanner;
    @Bind(R.id.sn_donate_buttton) Button donateButton;

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

    private void setUpButtons() {

        // DONATE BUTTON: I'M FEELING LUCKY CHARITY BUTTON
        donateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url; // Used to store the randomized charity site.
                int NUM_SITES; // Number of sites available to use from for the charity page.
                int randInt; // A random integer value within the range of NUM_SITES.
                Random randValue = new Random(); // A random generated value.

                NUM_SITES = 5; // Number of sites available to load.
                randInt = randValue.nextInt(NUM_SITES); // A random integer value within the range of NUM_SITES.

                // Switch case in which a background image is chosen from based on the randInt value.
                switch (randInt) {

                    case 0:
                        url = "http://www.accion.org/";
                        break;

                    case 1:
                        url = "http://www.mercycorps.org/";
                        break;

                    case 2:
                        url = "https://donate.doctorswithoutborders.org/";
                        break;

                    case 3:
                        url = "http://www.interaction.org/";
                        break;

                    case 4:
                        url = "https://engage.rescue.org";
                        break;

                    case 5:
                        url = "https://www.icrc.org/";
                        break;

                    default:
                        url = "http://www.redcross.org/";
                        break;
                }

                SNIntent.launchWebIntent(url, currentActivity);
            }
        });

        // DOCTORS WITHOUT BORDERS:
        dwbBanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNIntent.launchWebIntent("https://donate.doctorswithoutborders.org/", currentActivity);
            }
        });

        // INTERACTION:
        iaBanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNIntent.launchWebIntent("http://www.interaction.org/", currentActivity);
            }
        });

        // INTERNATIONAL RESCUE COMMITTEE:
        icrcBanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNIntent.launchWebIntent("https://engage.rescue.org", currentActivity);
            }
        });

         // INTERNATIONAL RED CROSS:
        ircBanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNIntent.launchWebIntent("https://www.icrc.org/", currentActivity);
            }
        });
    }

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