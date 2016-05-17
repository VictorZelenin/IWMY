package com.jacksonsmolenko.iwmy.cooltools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager for fragments in a single activity
 */
public abstract class CoolFragmentManager {

    private static final String TAG = "";

    private static FragmentManager fragmentManager;
    private static int fragmentHolderId;
    private static List<Fragment> fragmentStack = new ArrayList<>();

    /**
     * Remembers activity and fragment holder for the underlying functions
     *
     * @param activity               FragmentActivity which has fragments
     * @param fragmentHolderIdPassed ID of the fragment holding view
     */
    public static void setup(final FragmentActivity activity, final int fragmentHolderIdPassed) {
        fragmentManager = activity.getSupportFragmentManager();
        fragmentHolderId = fragmentHolderIdPassed;
    }

    /**
     * Shows the fragment, removing all other fragments from the stack.
     *
     * @param fragment the fragment to show
     */
    public static void showAtBottom(Fragment fragment) {
        showAtBottom(fragment, null);
    }

    /**
     * Shows the fragment, removing all other fragments from the stack.
     *
     * @param fragment the fragment to show
     * @param object   the object attached to the fragment
     */
    public static void showAtBottom(Fragment fragment, Serializable object) {
        clear();
        push(fragment);
        replaceWith(fragment, object);
    }

    /**
     * Shows the fragment at top of the stack.
     *
     * @param fragment the fragment to show
     */
    public static void showAtTop(Fragment fragment) {
        showAtTop(fragment, null);
    }

    /**
     * Shows the fragment at top of the stack.
     *
     * @param fragment the fragment to show
     * @param object   the object attached to the fragment
     */
    public static void showAtTop(Fragment fragment, Serializable object) {
        push(fragment);
        replaceWith(fragment, object);
    }

    /**
     * Shows the fragment.
     *
     * @param fragment the fragment to show
     * @param object   the object attached to the fragment
     */
    public static void show(Fragment fragment, Serializable object) {
        pop().onPause();
        push(fragment);
        replaceWith(fragment, object);
    }

    /**
     * Shows the previous fragment.
     */
    public static void showPrevious() {
        pop().onDetach();
        Fragment fragment = pop();
        if (fragment != null) {
            push(fragment);
            replaceWith(fragment, null);
        }
    }

    /**
     * Retrieves the attached object from the fragment
     *
     * @param fragment fragment which has the attached object
     */
    public static Serializable getAttachment(Fragment fragment) {
        return fragment.getArguments().getSerializable(TAG);
    }

    /**
     * Indicates presence of fragments in the back stack
     *
     * @return boolean presence
     */
    public static boolean isNotEmpty() {
        return (! fragmentStack.isEmpty());
    }

    private static void replaceWith(Fragment fragment, Serializable object) {
        fragment = attachObject(fragment, object);
        fragmentManager.beginTransaction()
                .replace(fragmentHolderId, fragment)
                .commit();
        Log.d("IWMY", "SHOWING: " + fragment.getClass().getSimpleName()
                + " (" + fragmentStack.size() + " in stack)");
    }

    private static Fragment attachObject(Fragment fragment, Serializable object) {
        Bundle bundle = new Bundle();
        if (object != null) {
            bundle.putSerializable(TAG, object);
        }
        if (fragment.getArguments() == null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private static void clear() {
        while (isNotEmpty()) {
            pop().onDetach();
        }
    }

    private static Fragment pop() {
        if (isNotEmpty()) {
            return fragmentStack.remove(fragmentStack.size() - 1);
        } else {
            return null;
        }
    }

    private static void push(Fragment fragment) {
        if (fragment != null) {
            fragmentStack.add(fragment);
        }
    }

}
