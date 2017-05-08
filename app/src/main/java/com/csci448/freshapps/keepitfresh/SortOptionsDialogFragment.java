package com.csci448.freshapps.keepitfresh;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.Date;

public class SortOptionsDialogFragment extends DialogFragment {
    public static final String EXTRA_SORT_OPTION =
            "com.csci448.freshapps.keepitfresh.sort_option_extra";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_menu_text)
                .setItems(R.array.sort_by_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(getActivity(), "Sort by name", Toast.LENGTH_SHORT)
                                        .show();
                                sendResult(Activity.RESULT_OK, SortOptions.NAME);

                                break;
                            case 1:
                                Toast.makeText(getActivity(), "Sort by expiration date",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                sendResult(Activity.RESULT_OK, SortOptions.EXPIRE);
                                break;
                            case 2:
                                Toast.makeText(getActivity(), "Sort by purchase date",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                sendResult(Activity.RESULT_OK, SortOptions.PURCHASE);
                                break;
                            default:
                                Toast.makeText(getActivity(), "this shouldn't happen",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                break;
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void sendResult(int resultCode, SortOptions option) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SORT_OPTION, option);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
