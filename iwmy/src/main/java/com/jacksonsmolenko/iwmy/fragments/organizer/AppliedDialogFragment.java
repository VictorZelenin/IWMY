package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.jacksonsmolenko.iwmy.R;

public class AppliedDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                .setMessage(R.string.dialog_registr_message_send)
//                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dismiss();
//                    }
//                });
        // Create the AlertDialog object and return it
//        return builder.create();
        return null;
    }
}
