package com.csci448.freshapps.keepitfresh;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.csci448.freshapps.keepitfresh.database.NewShoppingListItemDialogFragment;

import java.util.List;

/**
 * This class will hold data for items in our shopping list.
 */
public class ShoppingListActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_ITEM = 0;
    private static final String DIALOG_NEW_ITEM = "new_item";
    private RecyclerView mShoppingRecyclerView;
    private ListAdapter mAdapter;
    private StoredItems mStoredItems;
    private Dialog mNewItemDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mStoredItems = StoredItems.getInstance(this);
        setTitle(R.string.shopping_list);
        setContentView(R.layout.activity_shopping_list);

        mShoppingRecyclerView = (RecyclerView) findViewById(R.id.shopping_recycler_view);
        //in case we have to turn this into a fragment
        mShoppingRecyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListActivity.this));

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.menu_item_new_item:
                final EditText editText = new EditText(this);
                editText.setHint(R.string.hint_shopping_list_new_item);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(editText)
                    .setTitle(R.string.new_shopping_list_item_title)
                    .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Item newItem = new Item();
                            newItem.setName(editText.getText().toString());
                            newItem.setOnShoppingList(true);
                            mStoredItems.addItem(newItem);
                            updateUI();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            // TODO: 5/8/2017 create a settings option 
            case R.id.menu_item_delete_checked_items:
                for (Item i : mAdapter.mItems) {
                    if (i.isChecked()) {
                        i.setChecked(false);
                        i.setOnShoppingList(false);
                        mStoredItems.updateItem(i);
                    }
                }
                updateUI();
                return true;
            // TODO: 4/3/17 create a way to acquire checked items
//            case R.id.menu_item_acquire_checked_items:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStop() {
        super.onStop();

        List<Item> items = mAdapter.getItems();
        for (Item item : items) {
            StoredItems.getInstance(this).updateItem(item);
        }
    }

    private void updateUI() {
        List<Item> items = mStoredItems.getShoppingList();

        if (mAdapter == null) {
            mAdapter = new ListAdapter(items);
            mShoppingRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateItems(items);
        }
    }

    private class ListHolder extends RecyclerView.ViewHolder {
        private Item mItem;
        private TextView mName;
        private CheckBox mChecked;

        public ListHolder(View v) {
            super(v);

            mName = (TextView) v.findViewById(R.id.shopping_list_item_name);
            mChecked = (CheckBox) v.findViewById(R.id.shopping_list_item_checkbox);
        }

        public void bindItem(Item item) {
            mItem = item;
            mName.setText(mItem.getName());
            mChecked.setChecked(mItem.isChecked());
            mChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mItem.setChecked(isChecked);
                }
            });
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {
        private List<Item> mItems;

        public ListAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //find context for shopping list activity
            LayoutInflater inflater = LayoutInflater.from(ShoppingListActivity.this);
            View view = inflater.inflate(R.layout.list_item_shopping_list, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void updateItems(List<Item> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        public List<Item> getItems() {
            return mItems;
        }
    }
}
