package com.wolfcreations.mylistmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.wolfcreations.mylistmanager.adapter.ListDbAdapter;
import com.wolfcreations.mylistmanager.model.MyListItem;

import java.sql.SQLException;

/**
 * An activity representing a single MyListItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ListItemActivity}.
 */
public class ListItemDetailActivity extends AppCompatActivity  implements ListItemDetailFragment.AddItemListener{

    ListItemDetailFragment fragment;
    public static MyListItem CurrentListItem;
    private ListDbAdapter mDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CurrentListItem.toString());


        mDbAdapter = new ListDbAdapter(ListItemDetailActivity.this);
        try {
            mDbAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ListItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ListItemDetailFragment.ARG_ITEM_ID));
            fragment = new ListItemDetailFragment();

            fragment.setArguments(arguments);
            fragment.mItem = CurrentListItem;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.listitem_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ListItemActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddItem(MyListItem item) {
        // We get the item and return to the main activity
        //Log.d("TODO", "onAddItem");
        if (item.getId() == -1) { mDbAdapter.createlistitem(item);
        }
        else{
            mDbAdapter.updatelistitem(item);
        }
        Intent i = new Intent();
        if (getParent() == null) {
            setResult(RESULT_OK,i);
        } else {
            getParent().setResult(RESULT_OK,i);
        }
        finish();
    }
}
