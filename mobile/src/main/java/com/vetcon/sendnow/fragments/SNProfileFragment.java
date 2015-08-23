package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.data.SNTwitterUserModel;
import com.vetcon.sendnow.interfaces.OnFragmentUpdateListener;
import com.vetcon.sendnow.ui.toast.SNToast;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNProfileFragment extends Fragment {

    /** CLASS VARIABLES _____________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    // PROFILE VARIABLES
    private SNTwitterUserModel userProfile; // References the Twitter user's profile data model.

    // LOGGING VARIABLES
    private static final String LOG_TAG = SNProfileFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_profile_follow_button) Button followButton;
    @Bind(R.id.sn_profile_action_button_1) ImageButton cashButton;
    @Bind(R.id.sn_profile_action_button_2) ImageButton placeButton;
    @Bind(R.id.sn_profile_action_button_3) ImageButton messageButton;
    @Bind(R.id.sn_profile_twitter_button) ImageButton twitterButton;
    @Bind(R.id.sn_profile_image) ImageView profileImage;
    @Bind(R.id.sn_profile_name_text) TextView profileName;
    @Bind(R.id.sn_profile_twitter_text) TextView twitterHandle;
    @Bind(R.id.sn_profile_location_text) TextView userLocation;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // SNProfileFragment(): Constructor method for this class.
    private final static SNProfileFragment profile_fragment = new SNProfileFragment();

    // SNProfileFragment(): Deconstructor for this class.
    public SNProfileFragment() {}

    // getInstance(): Returns the profile_fragment instance.
    public static SNProfileFragment getInstance() { return profile_fragment; }

    // initializeFragment(): Initializes the fragment with the profile properties.
    public void initializeFragment(SNTwitterUserModel user) {
        this.userProfile = user;
    }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        View profile_view = (ViewGroup) inflater.inflate(R.layout.sn_profile_fragment_layout, container, false);
        ButterKnife.bind(this, profile_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return profile_view;
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {
        setUpButtons(); // Sets up the button listeners for the fragment.
        setUpImages(); // Sets up the ImageView objects for the fragment.
        setUpText(); // Sets up the TextView objects for the fragment.
    }

    // setUpButtons():
    private void setUpButtons() {

        // CASH BUTTON:
        cashButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Signals the parent activity to display the SNCalculateFragment.
                displayFragment("CALCULATE");
            }
        });

        // PLACE BUTTON:
        placeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Signals the parent activity to display the SNMapsFragment.
                displayFragment("MAPS");
            }
        });

        // MESSAGE BUTTON:
        messageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayFragment("DOCUMENTS");
            }
        });
    }

    // setUpImages(): Loads the images into the ImageView objects for the fragment
    private void setUpImages() {

        //TODO: Once Twitter Digits credentials is finished, uncomment and load the user's image here.
        /*
        // USER PROFILE IMAGE:
        Picasso.with(currentActivity)
                .load(userProfile.getUserImage())
                .into(profileImage);
                */
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // TODO: Once Twitter Digits credentials is finished, uncomment and load the user's data here.
        /*
        // Sets the logged in user's information into the TextView objects.
        profileName.setText(userProfile.getUserName());
        twitterHandle.setText(userProfile.getUserHandle());
        userLocation.setText(userProfile.getUserLocation());
        */
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    // displayFragment(): Displays the specified fragment.
    private void displayFragment(String fragType) {
        try { ((OnFragmentUpdateListener) currentActivity).displayFragment(fragType, 0); }
        catch (ClassCastException cce) {} // Catch for class cast exception errors.
    }
}
