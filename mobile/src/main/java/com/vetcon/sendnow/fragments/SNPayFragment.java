package com.vetcon.sendnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.interfaces.OnFragmentUpdateListener;
import com.vetcon.sendnow.interfaces.SimplifyInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNPayFragment extends DialogFragment implements Simplify.AndroidPayCallback {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // VALUE VARIABLES
    private double sentValue = 0;

    // SERVER VARIABLES
    private final static String HOST_URL = "http://ec2-52-10-243-201.us-west-2.compute.amazonaws.com";
    private final static String POST_POINT = "/api/fund";
    private final static String SENDERMAIL = "user1@sendnow.com";

    // LOGGING VARIABLES
    private static final String LOG_TAG = SNPayFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // ACTIVITY VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    @Bind(R.id.simplify_card) CardEditor simpCard;
    @Bind(R.id.simplify_button) Button simpButton;
    @Bind(R.id.sn_pay_send_value_text) TextView payValueText;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SNPayFragment pay_fragment = new SNPayFragment();

    public SNPayFragment() {}

    // getInstance(): Returns the pay_fragment instance.
    public static SNPayFragment getInstance() { return pay_fragment; }

    // initializeFragment(): Initializes the fragment.
    public void initializeFragment(double value) {
        this.sentValue = value;
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
        View pay_view = (ViewGroup) inflater.inflate(R.layout.sn_pay_fragment_layout, container, false);
        ButterKnife.bind(this, pay_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return pay_view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // register Android Pay callback
        Simplify.addAndroidPayCallback(this);
    }

    @Override
    public void onStop() {

        // remove Android Pay callback
        Simplify.removeAndroidPayCallback(this);
        super.onStop();
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setUpLayout() {

        setUpButtons();

        // PAY VALUE TEXT:
        payValueText.setText("$" + sentValue);
        payValueText.setShadowLayer(8, 0, 0, 0);
    }

    private void setUpButtons() {

        simpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestCardToken();
            }
        });
    }

    /** SERVER METHODS _________________________________________________________________________ **/

    private void sendToServer(CardToken card) {

        String token = card.getId(); // Retrieves the actual token.

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HOST_URL)
                .build();

        SimplifyInterface apiService = restAdapter.create(SimplifyInterface.class);

        apiService.sendToken(token, sentValue, SENDERMAIL, new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                Log.d(LOG_TAG, "SUCCESS! " + response);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "FAILURE! " + error);
            }
        });
    }

    /** SIMPLIFY METHODS _______________________________________________________________________ **/

    private void requestCardToken() {

        simpButton.setEnabled(true);

        Simplify.createCardToken(simpCard.getCard(), new CardToken.Callback() {

            @Override
            public void onSuccess(CardToken cardToken) {

                Log.d(LOG_TAG, "requestCardToken(): Card ID Received: " + cardToken.getId());
                Log.d(LOG_TAG, "requestCardToken(): Card Received: " + cardToken.getCard());
                Log.d(LOG_TAG, "requestCardToken(): Card Token Received: " + cardToken);

                sendToServer(cardToken); // Sends to the AWS server.
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(LOG_TAG, "requestCardToken(): Error encountered: " + throwable);
            }
        });
    }

    @Override
    public void onReceivedMaskedWallet(MaskedWallet maskedWallet) {

    }

    @Override
    public void onReceivedFullWallet(FullWallet fullWallet) {

    }

    @Override
    public void onAndroidPayCancelled() {

    }

    @Override
    public void onAndroidPayError(int i) {

    }
}
