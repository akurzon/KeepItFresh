package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class ItemDetailFragment extends Fragment {

    private static final String ITEM_ID = "item_id";

    private TextView mTitle, mExpireDate, mPurchaseDate, mLocation, mQuantity;
    private Button mEditButton;

    private Item mItem;

    public static ItemDetailFragment newInstance(long itemId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEM_ID, itemId);

        ItemDetailFragment fragment = new ItemDetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);
        // TODO: 4/2/2017 Add a button that will add the item to the shopping list 
/*
        mTitle = (TextView) v.findViewById(R.id.item_title);
        mExpireDate = (TextView) v.findViewById(R.id.item_expire_date_text);
        mPurchaseDate = (TextView) v.findViewById(R.id.item_purchase_date_text);
        mLocation = (TextView) v.findViewById(R.id.item_location_text);
        mQuantity = (TextView) v.findViewById(R.id.item_quantity_text);

        mTitle.setText(mItem.getName());
        // TODO: 3/2/17 get better date formatting
        // TODO: 3/2/17 change to string resource with insert formatting
        mExpireDate.setText(mItem.getExpirationDate().toString());
        mPurchaseDate.setText(mItem.getPurchaseDate().toString());
        mLocation.setText(mItem.getLocation());
        mQuantity.setText(mItem.getQuantity());

        mEditButton = (Button) v.findViewById(R.id.item_detail_edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
*/
    
        return v;
    }
}
