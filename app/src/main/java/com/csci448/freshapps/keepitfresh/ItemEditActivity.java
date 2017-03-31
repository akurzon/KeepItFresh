package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class ItemEditActivity extends AppCompatActivity {

    public static final String ITEM_ID = "item_id";

    private EditText mTitle, mLocation, mQuantity;
    private DatePicker mExpireDate, mPurchaseDate;
    private Button mSaveButton;

    private Item mItem;

    @Override
    protected void onCreate(Bundle bunduru) {
        super.onCreate(bunduru);
        long itemId = getIntent().getLongExtra(ITEM_ID, -1);
        setContentView(R.layout.fragment_edit_item);
        mItem = StoredItems.getInstance().getItem(itemId);

        mTitle = (EditText) findViewById(R.id.edit_item_title);
        mLocation = (EditText) findViewById(R.id.edit_item_location);
        mQuantity = (EditText) findViewById(R.id.edit_item_quantity);

        // TODO: 3/31/17 YO DAWG add greyed out instruction text to the textbox for title if it's empty
        mTitle.setText(mItem.getName());
        // TODO: 3/31/17 fix to use table converting int to category name as string
        // TODO: 3/31/17 also make location a dropdown
        // TODO: 3/31/17 setup database to have info about the uhhh categories
        mLocation.setText(String.valueOf(mItem.getLocation()));
        mQuantity.setText(String.valueOf(mItem.getQuantity()));

        // TODO: 3/31/17 change to text boxes that open new activities which hold the date picker on click
        mExpireDate = (DatePicker) findViewById(R.id.edit_item_expiration_date);
        mPurchaseDate = (DatePicker) findViewById(R.id.edit_item_purchase_date);

        mSaveButton = (Button) findViewById(R.id.item_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setName(mTitle.getText().toString());
                //location is an int, must find a way to set to int
                //mItem.setLocation(mLocation.getText().toInt());
                //quantity is an int
                mItem.setQuantity(Integer.parseInt(mLocation.getText().toString()));
                //do expire and purchase dates
                mItem.setExpirationDate(getDateFromDatePicker(mExpireDate));
                mItem.setPurchaseDate(getDateFromDatePicker(mPurchaseDate));
                StoredItems.getInstance().updateItem(mItem);
                // TODO: 3/31/17 exit once saved to pager view for this item
                onBackPressed();
            }
        });
        //TODO: 3/29/17 onclicklistener to save the item data to database, close activity
    }

    private Date getDateFromDatePicker(DatePicker picker) {
        Calendar c = Calendar.getInstance();
        c.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

        return c.getTime();
    }



//    @Override
//    protected Fragment createFragment() {
//        //TODO: 3/29/17 call the new instance function, I don't believe my implementation works because of database changes
//        //return ItemEditFragment.newInstance();
//        return new ItemEditFragment();
//    }
}
