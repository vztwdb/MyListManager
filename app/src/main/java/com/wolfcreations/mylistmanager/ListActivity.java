package com.wolfcreations.mylistmanager;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolfcreations.mylistmanager.adapter.ListDbAdapter;
import com.wolfcreations.mylistmanager.adapter.MyListAdapter;
import com.wolfcreations.mylistmanager.model.MyList;

import java.sql.SQLException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView mListView;
    private ListDbAdapter mDbAdapter;
    private ArrayAdapter<MyList> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent listItemintent = new Intent(ListActivity.this,ListItemActivity.class);
            ListItemActivity.CurrentList = null;
            ListItemActivity.SearchText = query;
            startActivity(listItemintent);
        }else {
            setContentView(R.layout.activity_list);
            mListView = (ListView) findViewById(R.id.listView);
            mListView.setDivider(null);
            mDbAdapter = new ListDbAdapter(ListActivity.this);
            try {
                mDbAdapter.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            arrayAdapter = new MyListAdapter(this, mDbAdapter.getAllList());
            mListView.setAdapter(arrayAdapter);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            //when we click an individual item in the listview
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                    ListView modeListView = new ListView(ListActivity.this);
                    String[] modes = new String[]{"Edit list", "Delete list"};
                    ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(ListActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                    modeListView.setAdapter(modeAdapter);
                    builder.setView(modeListView);
                    final Dialog dialog = builder.create();
                    dialog.show();
                    modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //edit list
                            if (position == 0) {
                                fireCustomDialog(arrayAdapter.getItem(masterListPosition));
                                //delete list
                            } else {
                                mDbAdapter.deleteListById((int) arrayAdapter.getItemId(masterListPosition));
                                arrayAdapter.clear();
                                arrayAdapter.addAll(mDbAdapter.getAllList());
                            }
                            dialog.dismiss();
                        }
                    });
                    return true;
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                    OpenListItemList(arrayAdapter.getItem(masterListPosition));
                }
            });

         }
    }

    private void OpenListItemList(MyList myList) {
        Intent intent = new Intent(ListActivity.this,ListItemActivity.class);
        ListItemActivity.CurrentList = myList;
        ListItemActivity.SearchText = null;
        startActivity(intent);
    }

    private void fireCustomDialog(final MyList myList) {
        // Spinner element
        final Spinner spinner;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (myList != null);
        // Spinner element
        spinner = (Spinner) dialog.findViewById(R.id.spinner);
        // Loading spinner data from database
        // Spinner Drop down elements
        List<String> categories = mDbAdapter.getAllCategories();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //this is for an edit
        if (isEditOperation) {
            titleView.setText("Edit list");
            checkBox.setChecked(myList.getPriority() == 1);
            editCustom.setText(myList.getName());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.maroon));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myListText = editCustom.getText().toString();
                if (isEditOperation) {
                    MyList listEdited = new MyList(myList.getId(),
                            myListText, checkBox.isChecked() ? 1 : 0);
                    listEdited.setCategory((String) spinner.getSelectedItem());
                    mDbAdapter.updateList(listEdited);
                } else {
                    MyList listEdited = new MyList(-1,
                            myListText, checkBox.isChecked() ? 1 : 0);
                    listEdited.setCategory((String) spinner.getSelectedItem());
                    mDbAdapter.createlist(listEdited);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(mDbAdapter.getAllList());
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                //create new list
                fireCustomDialog(null);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }
}
