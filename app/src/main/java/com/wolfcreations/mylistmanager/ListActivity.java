package com.wolfcreations.mylistmanager;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;

public class ListActivity extends AppCompatActivity {

    private ListView mListView;
    private ListDbAdapter mDbAdapter;
    private ListSimpleCursorAdapter mListSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setDivider(null);
        mDbAdapter = new ListDbAdapter(ListActivity.this);
        try {
            mDbAdapter.open();
            // test commentaar
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            //Clear all data
            //mDbAdapter.deleteAllLists();
        }
        Cursor cursor = mDbAdapter.fetchAllList();
        //from columns defined in the db
        String[] from = new String[]{
                ListDbAdapter.COL_NAME
        };
        //to the ids of views in the layout
        int[] to = new int[]{
                R.id.row_text
        };
        mListSimpleCursorAdapter = new ListSimpleCursorAdapter(
                //context
                ListActivity.this,
//the layout of the row
                R.layout.list_row,
//cursor
                cursor,
//from columns defined in the db
                from,
//to the ids of views in the layout
                to,
//flag - not used
                0);
// the cursorAdapter (controller) is now updating the listView (view)
//with data from the db (model)
        mListView.setAdapter(mListSimpleCursorAdapter);

      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.row_text,
                new String[]{"Videos", "Stripverhalen", "Adressen", "varia"});
        mListView.setAdapter(arrayAdapter);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //when we click an individual item in the listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                ListView modeListView = new ListView(ListActivity.this);
                String[] modes = new String[]{"Editeer lijst", "Detail Lijst", "Verwijder lijst"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(ListActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //edit reminder
                        if (position == 0) {
                            int nId = getIdFromPosition(masterListPosition);
                            MyList reminder = mDbAdapter.fetchListById(nId);
                            fireCustomDialog(reminder);
                        //delete reminder
                        } else if (position == 1) {
                            int nId = getIdFromPosition(masterListPosition);
                            MyList reminder = mDbAdapter.fetchListById(nId);
                            OpenListItemList(reminder);
                            //delete reminder
                        }else {
                            mDbAdapter.deleteListById(getIdFromPosition(masterListPosition));
                            mListSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllList());
                        }
                        dialog.dismiss();
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                                checked) {
                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.cam_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_delete_list:
                                    for (int nC = mListSimpleCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                        if (mListView.isItemChecked(nC)) {
                                            mDbAdapter.deleteListById(getIdFromPosition(nC));
                                        }
                                    }
                                    mode.finish();
                                    mListSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllList());
                                    return true;
                            }
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                        }
                    });
                }
            }
        });
    }

    private void OpenListItemList(MyList myList) {
        Intent intent = new Intent(ListActivity.this,ListItemListActivity.class);
        intent.putExtra(ListItemDetailFragment.ARG_ITEM_ID, myList.getName());
        startActivity(intent);
    }


    private int getIdFromPosition(int nC) {
        return (int) mListSimpleCursorAdapter.getItemId(nC);
    }


    private void fireCustomDialog(final MyList myList) {
// custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (myList != null);
//this is for an edit
        if (isEditOperation) {
            titleView.setText("Aanpassen lijst");
            checkBox.setChecked(myList.getPriority() == 1);
            editCustom.setText(myList.getName());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    MyList reminderEdited = new MyList(myList.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateList(reminderEdited);
//this is for new reminder
                } else {
                    mDbAdapter.createlist(reminderText, checkBox.isChecked(), "General");
                }


                mListSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllList());
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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView =
          //      (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new:
            //create new list
                fireCustomDialog(null);
                return true;
            case R.id.action_add:
                //create new list
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }
}
