package com.csci448.freshapps.keepitfresh;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;


public class ItemDetailFragment extends Fragment implements View.OnClickListener {

    private static final String ITEM_ID = "item_id";
    private static final int REQUEST_EDIT_ITEM = 0;

    private EditText mTitle;
    private Spinner mLocation;
    private Date mExpireDate, mPurchaseDate;
    private TextView mExpireDateTextView, mPurchaseDateTextView;
    private TextView mExpireDateHeader, mPurchaseDateHeader;
    private TextView mQuantityHeader, mQuantity;
    private DatePickerDialog mExpireDatePickerDialog, mPurchaseDatePickerDialog;
    private Dialog mNumberPickerDialog;
    private SimpleDateFormat mDateFormat;
    private Button mSaveButton, mShoppingButton, mDeleteButton;

    private Item mItem;
    private StoredItems mStoredItems;
    private boolean mItemSaved = false;
    private boolean mIsNewItem;

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
        mStoredItems = StoredItems.getInstance(getContext());
        UUID itemId = (UUID) getArguments().getSerializable(ITEM_ID);
        mIsNewItem = (itemId == null);
        if (mIsNewItem) {
            mItem = new Item();
        }
        else {
            mItem = mStoredItems.getItem(itemId);
        }
        mDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        findViewsById(v);
        updateUI();

        setupListeners();
        setupDatePickerDialogues();
        setupNumberPickerDialog();
        setupLocationSpinner(v);
        
        return v;
    }

    // TODO: 5/7/2017 check this
    // this may not work as intended
    @Override
    public void onStop() {
        super.onStop();
        if (!mItemSaved && mIsNewItem) {
            mStoredItems.deleteItem(mItem);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
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
        mTitle = (EditText) v.findViewById(R.id.edit_item_title);
        mQuantityHeader = (TextView) v.findViewById(R.id.edit_quantity_header);
        mQuantity = (TextView) v.findViewById(R.id.edit_item_quantity);
        mExpireDateTextView = (TextView) v.findViewById(R.id.expiration_date_text_view);
        mPurchaseDateTextView = (TextView) v.findViewById(R.id.purchase_date_text_view);
        mExpireDateHeader = (TextView) v.findViewById(R.id.edit_expiration_date_header);
        mPurchaseDateHeader = (TextView) v.findViewById(R.id.edit_purchase_date_header);
        mSaveButton = (Button) v.findViewById(R.id.item_save_button);
        mShoppingButton = (Button) v.findViewById(R.id.item_detail_shopping_button);
        mDeleteButton = (Button) v.findViewById(R.id.item_detail_delete_button);
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
                    showToast(R.string.error_quantity);
                    return;
                }
                if (mTitle.getText().toString().equals("")) {
                    showToast(R.string.error_no_name);
                    return;
                }

                mItem.setName(mTitle.getText().toString());
                mItem.setExpirationDate(mExpireDate);
                mItem.setPurchaseDate(mPurchaseDate);
                mItem.setQuantity(Integer.valueOf(mQuantity.getText().toString()));
                mItem.setLocation(mLocation.getSelectedItem().toString());

                mItemSaved = true;
                if (mIsNewItem) {
                    mStoredItems.addItem(mItem);
                    mIsNewItem = false;
                }
                // TODO: 5/7/2017 null object reference here
                mStoredItems.updateItem(mItem);
                // TODO: 5/7/2017 figure out how to go back to fragment
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });

        mShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setOnShoppingList(true);
                mItem.setChecked(false);
                String s = getString(R.string.toast_add_to_shopping);
                showToast(s);
                StoredItems.getInstance(getActivity()).updateItem(mItem);
            }
        });


        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoredItems.deleteItem(mItem);
                getActivity().finish();
            }
        });
    }

    private void updateUI() {
        //mItem = mStoredItems.getItem(mItem.getId());
        mTitle.setText(mItem.getName());
        mExpireDateTextView.setText(mDateFormat.format(mItem.getExpirationDate()));
        mPurchaseDateTextView.setText(mDateFormat.format(mItem.getPurchaseDate()));
        mQuantity.setText(String.valueOf(mItem.getQuantity()));
    }

    private void setupDatePickerDialogues() {
        mPurchaseDate = mItem.getPurchaseDate();
        mExpireDate = mItem.getExpirationDate();
        mExpireDateTextView.setText(mDateFormat.format(mItem.getExpirationDate()));
        mPurchaseDateTextView.setText(mDateFormat.format(mItem.getPurchaseDate()));

        Calendar calendar = Calendar.getInstance();

        mExpireDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                getActivity(), new DatePickerDialog.OnDateSetListener() {
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
        mNumberPickerDialog = new Dialog(getActivity());
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

    private void setupLocationSpinner(View v) {
        mLocation = (Spinner) v.findViewById(R.id.item_location_spinner);
        ArrayAdapter<String> locationArray = new ArrayAdapter<>(
                getActivity(),
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

    /**
     * Helper function for showing toasts
     * @param string string to be displayed in toast
     */
    private void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper function for showing toasts
     * @param resId res id of string to be displayed in toast
     */
    private void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

}
