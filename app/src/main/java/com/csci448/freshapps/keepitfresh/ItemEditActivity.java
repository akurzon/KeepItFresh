package com.csci448.freshapps.keepitfresh;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Most of the code in here is going to become obsolete. We are moving all this functionality to the detail.
 */
public class ItemEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_ITEM_ID = "item_id";
    private static final String EXTRA_NEW_ITEM = "new_item";

    private EditText mTitle;
    private Spinner mLocation;
    private Date mExpireDate, mPurchaseDate;
    private TextView mExpireDateTextView, mPurchaseDateTextView;
    private TextView mExpireDateHeader, mPurchaseDateHeader;
    private TextView mQuantityHeader, mQuantity;
    private Button mSaveButton;
    private DatePickerDialog mExpireDatePickerDialog, mPurchaseDatePickerDialog;
    private Dialog mNumberPickerDialog;
    private SimpleDateFormat mDateFormat;

    private Item mItem;
    private StoredItems mStoredItems;
    private boolean mItemSaved = false;
    private boolean mIsNewItem;

    @Override
    protected void onCreate(Bundle bunduru) {
        super.onCreate(bunduru);
        setTitle(R.string.edit_item);
        mStoredItems = StoredItems.getInstance(getApplicationContext());
        mIsNewItem = getIntent().getBooleanExtra(EXTRA_NEW_ITEM, false);
        UUID itemId;
        if (mIsNewItem) {
            mItem = new Item();
        }
        else {
            itemId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);
            mItem = mStoredItems.getItem(itemId);
        }
        setContentView(R.layout.activity_edit_item);
        findViewsById();

        mTitle.setText(mItem.getName());

        mDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        setupDatePickerDialogues();
        setupNumberPickerDialog();
        setupLocationSpinner();
        setupListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mItemSaved && mIsNewItem) {
            mStoredItems.deleteItem(mItem);
        }
    }

    public static Intent newIntent(Context context, UUID itemId, boolean isNewItem) {
        Intent intent = new Intent(context, ItemEditActivity.class);
        intent.putExtra(EXTRA_NEW_ITEM, isNewItem);
        intent.putExtra(EXTRA_ITEM_ID, itemId);

        return intent;
    }

    private void setupListeners() {
        mExpireDateTextView.setOnClickListener(this);
        mPurchaseDateTextView.setOnClickListener(this);
        mExpireDateHeader.setOnClickListener(this);
        mPurchaseDateHeader.setOnClickListener(this);
        mQuantityHeader.setOnClickListener(this);
        mQuantity.setOnClickListener(this);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // test for non zero quantity and non empty name field before saving
                if (mQuantity.getText().toString().equals(0)) {
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

                mItemSaved = true;
                if (mIsNewItem) {
                    mStoredItems.addItem(mItem);
                    mIsNewItem = false;
                }
                mStoredItems.updateItem(mItem);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void findViewsById() {
        mTitle = (EditText) findViewById(R.id.edit_item_title);
        mQuantityHeader = (TextView) findViewById(R.id.edit_quantity_header);
        mQuantity = (TextView) findViewById(R.id.edit_item_quantity);
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

    private void setupNumberPickerDialog() {
        mQuantity.setText(String.valueOf(mItem.getQuantity()));
        mNumberPickerDialog = new Dialog(this);
        mNumberPickerDialog.setTitle(R.string.quantity_set);
        mNumberPickerDialog.setContentView(R.layout.number_dialog);
        Button cancelButton = (Button) mNumberPickerDialog.findViewById(R.id.button_cancel);
        Button setButton = (Button) mNumberPickerDialog.findViewById(R.id.button_set);
        final NumberPicker picker = (NumberPicker) mNumberPickerDialog.findViewById(R.id.quantity_picker);
        picker.setMaxValue(100);
        picker.setMinValue(1);
        picker.setWrapSelectorWheel(false);
        //picker.setOnValueChangedListener(NumberPicker.OnValueChangeListener());

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity.setText(String.valueOf(picker.getValue()));
                mNumberPickerDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberPickerDialog.dismiss();
            }
        });
    }

    private void setupLocationSpinner() {
        mLocation = (Spinner) findViewById(R.id.item_location_spinner);
        ArrayAdapter<String> locationArray = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                mStoredItems.getLocations());
        mLocation.setAdapter(locationArray);
    }

    @Override
    public void onClick(View v) {

        if (v == mExpireDateTextView || v == mExpireDateHeader) {
            mExpireDatePickerDialog.show();
        } else if (v == mPurchaseDateTextView || v == mPurchaseDateHeader) {
            mPurchaseDatePickerDialog.show();
        } else if (v == mQuantityHeader || v == mQuantity) {
            mNumberPickerDialog.show();
        }
    }

//    @Override
//    protected Fragment createFragment() {
//        //TODO: 3/29/17 call the new instance function, I don't believe my implementation works because of database changes
//        //return ItemEditFragment.newInstance();
//        return new ItemEditFragment();
//    }
}
