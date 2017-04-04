package com.csci448.freshapps.keepitfresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;


public class ItemDetailFragment extends Fragment {

    private static final String ITEM_ID = "item_id";
    private static final int REQUEST_EDIT_ITEM = 0;

    private TextView mTitle, mExpireDate, mPurchaseDate, mLocation, mQuantity;
    private Button mEditButton, mShoppingButton;
    private SimpleDateFormat mDateFormat;

    private Item mItem;

    public static ItemDetailFragment newInstance(UUID itemId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEM_ID, itemId);

        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UUID itemId = (UUID) getArguments().getSerializable(ITEM_ID);
        mItem = StoredItems.getInstance(getContext()).getItem(itemId);
        mDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        findViewsById(v);
        updateUI();

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getActivity(), ItemEditActivity.class);
                editIntent.putExtra(ItemEditActivity.ITEM_ID, mItem.getId());
                startActivityForResult(editIntent, REQUEST_EDIT_ITEM);
            }
        });
        
        mShoppingButton = (Button) v.findViewById(R.id.item_detail_shopping_button);
        mShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 4/3/2017 Set the boolean for shopping list to true, and checked to false, update item, provide toast
                mItem.setOnShoppingList(true);
                mItem.setChecked(false);
                String s = getString(R.string.toast_add_to_shopping);
                showToast(s);
                StoredItems.getInstance(getActivity()).updateItem(mItem);
            }
        });
        
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_EDIT_ITEM:
                updateUI();
                break;
            default:
                updateUI();
                break;
        }
    }

    private void findViewsById(View v) {
        mTitle = (TextView) v.findViewById(R.id.item_title);
        mExpireDate = (TextView) v.findViewById(R.id.item_expire_date_text);
        mPurchaseDate = (TextView) v.findViewById(R.id.item_purchase_date_text);
        mLocation = (TextView) v.findViewById(R.id.item_location_text);
        mQuantity = (TextView) v.findViewById(R.id.item_quantity_text);
        mEditButton = (Button) v.findViewById(R.id.item_detail_edit_button);
    }

    private void updateUI() {
        mItem = StoredItems.getInstance(getContext()).getItem(mItem.getId());
        mTitle.setText(mItem.getName());
        mExpireDate.setText(mDateFormat.format(mItem.getExpirationDate()));
        mPurchaseDate.setText(mDateFormat.format(mItem.getPurchaseDate()));
        mLocation.setText(mItem.getLocation());
        mQuantity.setText(String.valueOf(mItem.getQuantity()));
    }

    private void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}
