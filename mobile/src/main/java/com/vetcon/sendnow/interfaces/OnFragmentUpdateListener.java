package com.vetcon.sendnow.interfaces;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public interface OnFragmentUpdateListener {

    // displayFragment(): Interface method which signals the parent activity to change the fragment
    // view.
    void displayFragment(String fragType);

    // removeFragment(): Interface method which signals the parent activity to remove the fragment
    // view.
    void removeFragment();
}
