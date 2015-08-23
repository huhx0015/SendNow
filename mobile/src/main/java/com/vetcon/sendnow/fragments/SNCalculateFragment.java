package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vetcon.sendnow.R;
import com.vetcon.sendnow.interfaces.OnFragmentUpdateListener;
import com.vetcon.sendnow.ui.toast.SNToast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNCalculateFragment extends Fragment {

    /** CLASS VARIABLES _____________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    // LOGGING VARIABLES
    private static final String LOG_TAG = SNCalculateFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_calculate_button_0) Button zeroButton;
    @Bind(R.id.sn_calculate_button_1) Button oneButton;
    @Bind(R.id.sn_calculate_button_2) Button twoButton;
    @Bind(R.id.sn_calculate_button_3) Button threeButton;
    @Bind(R.id.sn_calculate_button_4) Button fourButton;
    @Bind(R.id.sn_calculate_button_5) Button fiveButton;
    @Bind(R.id.sn_calculate_button_6) Button sixButton;
    @Bind(R.id.sn_calculate_button_7) Button sevenButton;
    @Bind(R.id.sn_calculate_button_8) Button eightButton;
    @Bind(R.id.sn_calculate_button_9) Button nineButton;
    @Bind(R.id.sn_calculate_decimal_button) Button decimalButton;
    @Bind(R.id.sn_calculate_delete_button) Button deleteButton;
    @Bind(R.id.sn_calculate_send_button) Button sendButton;
    @Bind(R.id.sn_calculate_request_button) Button requestButton;
    @Bind(R.id.sn_calculate_value_text) TextView valueText;

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
        setUpButtons();
        setUpText();
    }

    // setUpButtons(): Sets up the button listeners for the fragment.
    private void setUpButtons() {

        // REQUEST BUTTON:
        requestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // displayFragment("REQUEST");
            }
        });

        // SEND BUTTON:
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayFragment("PAY");
            }
        });

        // 0 BUTTON:
        zeroButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "0");
            }
        });

        // ONE BUTTON:
        oneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "1");
            }
        });

        // TWO BUTTON:
        twoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "2");
            }
        });

        // THREE BUTTON:
        threeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "3");
            }
        });

        // FOUR BUTTON:
        fourButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "4");
            }
        });

        // FIVE BUTTON:
        fiveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "5");
            }
        });

        // SIX BUTTON:
        sixButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "6");
            }
        });

        // SEVEN BUTTON:
        sevenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "7");
            }
        });

        // EIGHT BUTTON:
        eightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString() + "8");
            }
        });

        // NINE BUTTON:
        nineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString()  + "9");
            }
        });

        // DELETE BUTTON: Removes the last character from the TextView object.
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int textCount = valueText.getText().toString().length();

                // Only removes the last value if the text count is greater than 1 character.
                if (textCount > 1) {
                    valueText.setText(valueText.getText().toString().substring(0, textCount - 1));
                }
            }
        });

        // DECIMAL BUTTON:
        decimalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valueText.setText(valueText.getText().toString() + ".");
            }
        });
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // Sets the initial text for the valueText TextView object.
        valueText.setText("$");
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    // displayFragment(): Displays the specified fragment.
    private void displayFragment(String fragType) {
        try { ((OnFragmentUpdateListener) currentActivity).displayFragment(fragType); }
        catch (ClassCastException cce) {} // Catch for class cast exception errors.
    }
}