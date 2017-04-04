package com.csci448.freshapps.keepitfresh;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

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
    private TextView mExpireDateHeader, mPurchaseDateHeader;
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
        mQuantity.setHint(String.valueOf(mItem.getQuantity()));

        mDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        setupDatePickerDialogues();
        setupLocationSpinner();
//        setupSaveButton();
        setupListeners();
    }

    private void setupListeners() {
        //TODO: 3/29/17 onclicklistener to save the item data to database, close activity
        mExpireDateTextView.setOnClickListener(this);
        mPurchaseDateTextView.setOnClickListener(this);
        mExpireDateHeader.setOnClickListener(this);
        mPurchaseDateHeader.setOnClickListener(this);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // test for non zero quantity and non empty name field before saving
                if (mQuantity.getText().toString().equals("0")) {
                    Toast.makeText(getApplicationContext(), R.string.error_quantity,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.error_no_name,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mItem.setName(mTitle.getText().toString());
                mItem.setExpirationDate(mExpireDate);
                mItem.setPurchaseDate(mPurchaseDate);
                mItem.setQuantity(Integer.valueOf(mQuantity.getText().toString()));
                mItem.setLocation(mLocation.getSelectedItem().toString());
                // TODO: 4/3/17 set booleans for item

                StoredItems.getInstance(getApplicationContext()).updateItem(mItem);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void findViewsById() {
        mTitle = (EditText) findViewById(R.id.edit_item_title);
        mQuantity = (EditText) findViewById(R.id.edit_item_quantity);
        mExpireDateTextView = (TextView) findViewById(R.id.expiration_date_text_view);
        mPurchaseDateTextView = (TextView) findViewById(R.id.purchase_date_text_view);
        mExpireDateHeader = (TextView) findViewById(R.id.edit_expiration_date_header);
        mPurchaseDateHeader = (TextView) findViewById(R.id.edit_purchase_date_header);
        mSaveButton = (Button) findViewById(R.id.item_save_button);
    }

    private void setupDatePickerDialogues() {
        mPurchaseDate = mItem.getPurchaseDate();
        mExpireDate = mItem.getExpirationDate();
        mExpireDateTextView.setText(mDateFormat.format(mExpireDate));
        mPurchaseDateTextView.setText(mDateFormat.format(mPurchaseDate));

        Calendar calendar = Calendar.getInstance();

        mExpireDatePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mExpireDateTextView.setText(mDateFormat.format(newDate.getTime()));
                mExpireDate = newDate.getTime();
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
                mPurchaseDate = newDate.getTime();
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void setupLocationSpinner() {
        mLocation = (Spinner) findViewById(R.id.item_location_spinner);
        ArrayAdapter<String> locationArray = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                StoredItems.getInstance(getApplicationContext()).getLocations());
        mLocation.setAdapter(locationArray);
    }

    @Override
    public void onClick(View v) {

        if (v == mExpireDateTextView || v == mExpireDateHeader) {
            mExpireDatePickerDialog.show();
        } else if (v == mPurchaseDateTextView || v == mPurchaseDateHeader) {
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
