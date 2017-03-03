package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

public class ItemDetailFragment extends Fragment {

    private static final String ITEM_ID = "item_id";

    private TextView mTitle;

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
        mItem = StoredItems.getInstance().getItem(itemId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        mTitle = (TextView) v.findViewById(R.id.item_title);
        mTitle.setText(mItem.getName());

        return v;
    }
}
