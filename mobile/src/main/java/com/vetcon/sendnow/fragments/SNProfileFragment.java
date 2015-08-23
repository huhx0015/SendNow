package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.vetcon.sendnow.R;
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

    // LOGGING VARIABLES
    private static final String LOG_TAG = SNProfileFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_profile_action_button_1) ImageButton cashButton;
    @Bind(R.id.sn_profile_action_button_2) ImageButton placeButton;
    @Bind(R.id.sn_profile_action_button_3) ImageButton messageButton;
    @Bind(R.id.sn_profile_twitter_button) ImageButton twitterButton;
    @Bind(R.id.sn_profile_follow_button) Button followButton;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SNProfileFragment profile_fragment = new SNProfileFragment();

    public SNProfileFragment() {}

    // getInstance(): Returns the calculate_fragment instance.
    public static SNProfileFragment getInstance() { return profile_fragment; }

    // initializeFragment(): Initializes the fragment with the profile properties.
    public void initializeFragment() {}

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
    }

    // setUpButtons():
    private void setUpButtons() {

        // CASH BUTTON:
        cashButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO: Define action here.
                SNToast.toastyPopUp("CASH", currentActivity);
            }
        });

        // PLACE BUTTON:
        placeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO: Define action here.
                SNToast.toastyPopUp("PLACE", currentActivity);
            }
        });

        // MESSAGE BUTTON:
        messageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO: Define action here.
                SNToast.toastyPopUp("MESSAGE", currentActivity);
            }
        });
    }

    // setUpImages():
    private void setUpImages() { }
}
