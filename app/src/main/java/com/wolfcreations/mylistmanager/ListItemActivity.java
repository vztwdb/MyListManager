package com.wolfcreations.mylistmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.text.TextUtils;


import com.wolfcreations.mylistmanager.adapter.ListDbAdapter;
import com.wolfcreations.mylistmanager.adapter.SimpleItemRecyclerViewAdapter;
import com.wolfcreations.mylistmanager.model.Book;
import com.wolfcreations.mylistmanager.model.Movie;
import com.wolfcreations.mylistmanager.model.MyList;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.ToDo;

import java.sql.SQLException;
import java.text.ParseException;


public class ListItemActivity extends AppCompatActivity {

    public  static int ADD_ITEM = 100;

    private RecyclerView mRecyclerView;
    private SimpleItemRecyclerViewAdapter  mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static MyList CurrentList;
    public  static String SearchText;
    private ListDbAdapter mDbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (TextUtils.isEmpty(SearchText)) {
            getSupportActionBar().setTitle(CurrentList.toString());
        } else
        {
            getSupportActionBar().setTitle("Search on: '" +  SearchText + "'");
        }

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
            case R.id.action_add:
                Intent intent2 = new Intent(ListItemActivity.this, ListItemDetailActivity.class);
                if (CurrentList.getCategory().equals("Todo")) {
                    ListItemDetailActivity.CurrentListItem = new ToDo(CurrentList, -1, "","");
                }else if (CurrentList.getCategory().equals("Book")) {
                    ListItemDetailActivity.CurrentListItem = new Book(CurrentList, -1, "","");
                } else if (CurrentList.getCategory().equals("Movie") ) {
                    ListItemDetailActivity.CurrentListItem = new Movie(CurrentList, -1, "","");
                } else {
                    ListItemDetailActivity.CurrentListItem = new MyListItem(CurrentList, -1, "","");
                }
                startActivityForResult(intent2, ADD_ITEM);
                return true;
            default:
                return false; }

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        if (!TextUtils.isEmpty(SearchText)) {
            MenuItem item = menu.findItem(R.id.action_add);
            item.setVisible(false);
        }
        MenuItem itemsearch = menu.findItem(R.id.action_search);
        itemsearch.setVisible(false);
        return true;
    }


    private void setupRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            if (TextUtils.isEmpty(SearchText)) {
                mAdapter = new SimpleItemRecyclerViewAdapter(this, mDbAdapter.fetchListItemsByListid(CurrentList), mDbAdapter);
            } else
            {
                mAdapter = new SimpleItemRecyclerViewAdapter(this, mDbAdapter.fetchListItemsBySearchCriteria(SearchText), mDbAdapter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
