package com.wolfcreations.mylistmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.wolfcreations.mylistmanager.adapter.ListDbAdapter;
import com.wolfcreations.mylistmanager.adapter.SimpleItemRecyclerViewAdapter;
import com.wolfcreations.mylistmanager.dummy.DummyContent;
import com.wolfcreations.mylistmanager.model.MyList;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.ToDo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of ListItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ListItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListItemActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static int ADD_ITEM = 100;

    private RecyclerView mRecyclerView;
    private SimpleItemRecyclerViewAdapter  mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static MyList CurrentList;
    private ListDbAdapter mDbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CurrentList.toString());

        mDbAdapter = new ListDbAdapter(ListItemActivity.this);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.listitem_list);
        setupRecyclerView();
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_new:
                Intent intent = new Intent(ListItemActivity.this, ListItemDetailActivity.class);
                ListItemDetailActivity.CurrentListItem = new MyListItem(CurrentList, -1, "New Item","");
                startActivityForResult(intent, ADD_ITEM);
                return true;
            case R.id.action_add:
                Intent intent2 = new Intent(ListItemActivity.this, ListItemDetailActivity.class);
                ListItemDetailActivity.CurrentListItem = new MyListItem(CurrentList, -1, "","");
                startActivityForResult(intent2, ADD_ITEM);
                //Intent i2 = new Intent(this, ListItemDetailActivity.class);
                //startActivityForResult(i2, ADD_ITEM);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false; }

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }


    private void setupRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SimpleItemRecyclerViewAdapter(this,  mDbAdapter.fetchListItemsByListid(CurrentList));
       // mAdapter = new SimpleItemRecyclerViewAdapter( mDbAdapter.fetchListItemsBySearchCriteria("eschr"));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM) {
            if (resultCode == RESULT_OK) {
                setupRecyclerView();
            }
        }
    }

}
