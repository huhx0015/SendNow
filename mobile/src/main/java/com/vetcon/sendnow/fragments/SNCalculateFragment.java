package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vetcon.sendnow.R;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNCalculateFragment extends Fragment {

    /** CLASS VARIABLES _____________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String TAG = SNCalculateFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // ACTIVITY VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SNCalculateFragment calculate_fragment = new SNCalculateFragment();

    public SNCalculateFragment() {}

    // getInstance(): Returns the calculate_fragment instance.
    public static SNCalculateFragment getInstance() { return calculate_fragment; }

    // initializeFragment(): Initializes the fragment.
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
        View calculate_view = (ViewGroup) inflater.inflate(R.layout.sn_calculate_fragment_layout, container, false);
        ButterKnife.bind(this, calculate_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return calculate_view;
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

    }
}