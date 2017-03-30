package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by nate on 3/29/17.
 */

public class ItemEditFragment extends Fragment {

    private static final String ITEM_ID = "item_id";

    private EditText mTitle, mLocation, mQuantity;
    private DatePicker mExpireDate, mPurchaseDate;
    private Button mSaveButton;

    private Item mItem;

    public static ItemEditFragment newInstance(long itemId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEM_ID, itemId);

        ItemEditFragment fragment = new ItemEditFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        long itemId = getArguments().getLong(ITEM_ID);
        mItem = StoredItems.getInstance().getItem(itemId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_edit_item, container, false);

        mTitle = (EditText) v.findViewById(R.id.edit_item_title);
        mLocation = (EditText) v.findViewById(R.id.edit_item_location);
        mQuantity = (EditText) v.findViewById(R.id.edit_item_quantity);

        mTitle.setText(mItem.getName());
        mLocation.setText(mItem.getLocation());
        mQuantity.setText(mItem.getQuantity());

        mExpireDate = (DatePicker) v.findViewById(R.id.edit_item_expiration_date);
        mPurchaseDate = (DatePicker) v.findViewById(R.id.edit_item_purchase_date);

        mSaveButton = (Button) v.findViewById(R.id.item_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setName(mTitle.getText().toString());
                //location is an int, must find a way to set to int
                //mItem.setLocation(mLocation.getText().toString());
                //quantity is an int
                //mItem.setQuantity(mLocation.getText().toString());

                //do expire and purchase dates

            }
        });
        //TODO: 3/29/17 onclicklistener to save the item data to database, close activity

        return v;
    }
}
