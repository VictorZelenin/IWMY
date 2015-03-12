package com.oleksiykovtun.android.cooltools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;

/**
 * Manager for fragments in a single activity
 */
public abstract class CoolFragmentManager {

    private static final String TAG = "";

    private static FragmentManager fragmentManager;
    private static int fragmentHolderId;

    /**
     * Remembers activity and fragment holder for the underlying functions
     * @param activity FragmentActivity which has fragments
     * @param fragmentHolderIdPassed ID of the fragment holding view
     */
    public static void setup(final FragmentActivity activity, final int fragmentHolderIdPassed) {
        fragmentManager = activity.getSupportFragmentManager();
        fragmentHolderId = fragmentHolderIdPassed;
    }

    /**
     * Shows the fragment, removing all previous fragments.
     * Back button will remove this fragment
     * @param fragment the fragment to show
     */
    public static void switchToRootFragment(Fragment fragment) {
        switchToRootFragment(fragment, null);
    }

    /**
     * Shows the fragment, removing all previous fragments.
     * Back button will remove this fragment
     * @param fragment the fragment to show
     * @param object the object attached to the fragment
     */
    // debug not really root fragment
    public static void switchToRootFragment(Fragment fragment, Serializable object) {
        cleanBackStack();
        switchToFragment(fragment, object);
    }

    /**
     * Shows the fragment atop of the previous fragment.
     * Back button will return to the previous fragment in the stack
     * @param fragment the fragment to show
     */
    public static void switchToFragment(Fragment fragment) {
        switchToFragment(fragment, null);
    }

    /**
     * Shows the fragment atop of the previous fragment.
     * Back button will return to the previous fragment in the stack
     * @param fragment the fragment to show
     * @param object the object attached to the fragment
     */
    public static void switchToFragment(Fragment fragment, Serializable object) {
        fragment = attachObject(fragment, object);
        fragmentManager.beginTransaction()
                .replace(fragmentHolderId, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    /**
     * Shows the previous fragment. Acts like Back button
     */
    public static void switchToPreviousFragment() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * Retrieves the attached object from the fragment
     * @param fragment fragment which has the attached object
     */
    public static Serializable getAttachment(Fragment fragment) {
        return fragment.getArguments().getSerializable(TAG);
    }

    // todo document
    public static boolean areAnyOlderFragments() {
        return (fragmentManager.getBackStackEntryCount() > 0);
    }

    private static Fragment attachObject(Fragment fragment, Serializable object) {
        Bundle bundle = new Bundle();
        if (object != null) {
            bundle.putSerializable(TAG, object);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void removeThisFragment() {
        if (areAnyOlderFragments()) {
            fragmentManager.popBackStack();
        }
    }

    private static void cleanBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

}
