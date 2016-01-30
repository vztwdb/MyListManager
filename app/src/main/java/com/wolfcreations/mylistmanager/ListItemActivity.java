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

import com.wolfcreations.mylistmanager.dummy.DummyContent;
import com.wolfcreations.mylistmanager.model.MyListItem;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                Intent i = new Intent(this, ListItemDetailActivity.class);
                startActivityForResult(i, ADD_ITEM);
                return true;
            case R.id.action_add:
                Intent i2 = new Intent(this, ListItemDetailActivity.class);
                startActivityForResult(i2, ADD_ITEM);
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

        // specify an adapter (see also next example)
        String name =  getIntent().getStringExtra(ListItemDetailFragment.ARG_ITEM_ID);
        if (DummyContent.ITEM_MAP.size() == 0 || (name != DummyContent.Name)) {
            DummyContent dummy = new DummyContent(name);
        }
        mAdapter = new SimpleItemRecyclerViewAdapter( DummyContent.ITEMS);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("TODO", "OnResult");
        if (requestCode == ADD_ITEM) {
            if (resultCode == RESULT_OK) {
                //Log.d("TODO", "OK");
                MyListItem i = (MyListItem) data.getExtras().getSerializable("item");
                mAdapter.mValues.add(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

     public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        public final List<MyListItem> mValues;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        public SimpleItemRecyclerViewAdapter(List<MyListItem> items) {
                mValues = items;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_row, parent, false);
//                    .inflate(R.layout.listitem_list_content, parent, false);
            //R.layout.

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            //holder.mIdView.setText(mValues.get(position).getName());
            //holder.mContentView.setText(mValues.get(position).getComment());
            holder.nameView.setText(holder.mItem.getName());
            holder.descrView.setText(holder.mItem.getComment());
            holder.tagView.setBackgroundResource(holder.mItem.myTag.getTagColor());
            holder.dateView.setText(sdf.format(holder.mItem.duedate));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(ListItemActivity.this, ListItemDetailActivity.class);
                        intent.putExtra(ListItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public MyListItem getItem(int position) {
            if (mValues != null)
                return mValues.get(position);

            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mValues != null)
                return mValues.get(position).hashCode();

            return 0;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public  View mView;
            //public final TextView mIdView;
            //public final TextView mContentView;
            ImageView tagView;
            TextView nameView;
            TextView descrView;
            TextView dateView;
            public MyListItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id);
                //mContentView = (TextView) view.findViewById(R.id.content);

                // Look for Views in the layout
                tagView = (ImageView) view.findViewById(R.id.tagView);
                nameView = (TextView) view.findViewById(R.id.nameView);
                descrView = (TextView) view.findViewById(R.id.descrView);
                dateView = (TextView) view.findViewById(R.id.dateView);
                view.setTag(this);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + nameView.getText() + "'";
            }
        }
    }
}