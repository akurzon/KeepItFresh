package com.csci448.freshapps.keepitfresh;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ItemEditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ITEM_ID = "item_id";

    private EditText mTitle, mQuantity;
    private Spinner mLocation;
    private Date mExpireDate, mPurchaseDate;
    private TextView mExpireDateTextView, mPurchaseDateTextView;
    private Button mSaveButton;
    private DatePickerDialog mExpireDatePickerDialog, mPurchaseDatePickerDialog;
    private SimpleDateFormat mDateFormat;
    private Item mItem;

    @Override
    protected void onCreate(Bundle bunduru) {
        super.onCreate(bunduru);
        UUID itemId = (UUID) getIntent().getSerializableExtra(ITEM_ID);
        mItem = StoredItems.getInstance(getApplicationContext()).getItem(itemId);
        setContentView(R.layout.activity_edit_item);
        findViewsById();


        mTitle.setText(mItem.getName());
        mQuantity.setText(String.valueOf(mItem.getQuantity()));

        mDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setupDatePickerDialogues();

        setupLocationSpinner();
        setupSaveButton();

        mExpireDateTextView.setOnClickListener(this);
        mPurchaseDateTextView.setOnClickListener(this);

        //TODO: 3/29/17 onclicklistener to save the item data to database, close activity
    }

    private void findViewsById() {
        mTitle = (EditText) findViewById(R.id.edit_item_title);
        mQuantity = (EditText) findViewById(R.id.edit_item_quantity);
        mExpireDateTextView = (TextView) findViewById(R.id.expiration_date_text_view);
        mPurchaseDateTextView = (TextView) findViewById(R.id.expiration_date_text_view);
    }

    private void setupDatePickerDialogues() {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mExpireDatePickerDialog = new DatePickerDialog(
                    this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, month, dayOfMonth);
                    mExpireDateTextView.setText(mDateFormat.format(newDate.getTime()));
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            mPurchaseDatePickerDialog = new DatePickerDialog(
                    this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, month, dayOfMonth);
                    mPurchaseDateTextView.setText(mDateFormat.format(newDate.getTime()));
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    private void setupSaveButton() {
        mSaveButton = (Button) findViewById(R.id.item_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setName(mTitle.getText().toString());
                //quantity is an int
                mItem.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                //do expire and purchase dates
                // TODO: 3/31/17 fix this after you get info from actual datepicker (there should only be one)
//                mItem.setExpirationDate(mExpireDate);
//                mItem.setPurchaseDate(mPurchaseDate);
                StoredItems.getInstance(getApplicationContext()).updateItem(mItem);
                // TODO: 3/31/17 exit once saved to pager view for this item
                // TODO: 4/1/17

                onBackPressed();
            }
        });
    }

    private void setupLocationSpinner() {
        mLocation = (Spinner) findViewById(R.id.item_location_spinner);
        ArrayAdapter<String> locationArray = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                StoredItems.getInstance(getApplicationContext()).getLocations());
        mLocation.setAdapter(locationArray);
    }

//    private Date getDateFromDatePicker(DatePicker picker) {
//        Calendar c = Calendar.getInstance();
//        c.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
//
//        return c.getTime();
//    }

    @Override
    public void onClick(View v) {
        if(v == mExpireDateTextView) {
            mExpireDatePickerDialog.show();
        } else if(v == mPurchaseDateTextView) {
            mPurchaseDatePickerDialog.show();
        }
    }

//    @Override
//    protected Fragment createFragment() {
//        //TODO: 3/29/17 call the new instance function, I don't believe my implementation works because of database changes
//        //return ItemEditFragment.newInstance();
//        return new ItemEditFragment();
//    }
}
