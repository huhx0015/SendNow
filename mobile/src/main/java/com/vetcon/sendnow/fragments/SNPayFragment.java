package com.vetcon.sendnow.fragments;

import android.support.v4.app.DialogFragment;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.simplify.android.sdk.Simplify;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNPayFragment extends DialogFragment implements Simplify.AndroidPayCallback {



    /** SIMPLIFY METHODS _______________________________________________________________________ **/

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
