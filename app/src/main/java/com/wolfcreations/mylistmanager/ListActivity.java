package com.wolfcreations.mylistmanager;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import java.sql.SQLException;

public class ListActivity extends AppCompatActivity {

    private ListView mListView;
    private LijstDbAdapter mDbAdapter;
    private LijstSimpleCursorAdapter mLijstSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setDivider(null);
        mDbAdapter = new LijstDbAdapter(ListActivity.this);
        try {
            mDbAdapter.open();
            // test commentaar
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
//Clear all data
            //mDbAdapter.deleteAllLijsten();
//Add some data
            //addSomeData();
        }
        Cursor cursor = mDbAdapter.fetchAllLijst();
        //from columns defined in the db
        String[] from = new String[]{
                LijstDbAdapter.COL_NAAM
        };
        //to the ids of views in the layout
        int[] to = new int[]{
                R.id.row_text
        };
        mLijstSimpleCursorAdapter = new LijstSimpleCursorAdapter(
                //context
                ListActivity.this,
//the layout of the row
                R.layout.lijst_row,
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
        mListView.setAdapter(mLijstSimpleCursorAdapter);

      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.lijst_row, R.id.row_text,
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
                String[] modes = new String[]{"Editeer lijst", "Verwijder lijst"};
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
                            Lijst reminder = mDbAdapter.fetchLijstById(nId);
                            fireCustomDialog(reminder);
//delete reminder
                        } else {
                            mDbAdapter.deleteLijstById(getIdFromPosition(masterListPosition));
                            mLijstSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllLijst());
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
                                case R.id.menu_item_delete_reminder:
                                    for (int nC = mLijstSimpleCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                        if (mListView.isItemChecked(nC)) {
                                            mDbAdapter.deleteLijstById(getIdFromPosition(nC));
                                        }
                                    }
                                    mode.finish();
                                    mLijstSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllLijst());
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

    private int getIdFromPosition(int nC) {
        return (int) mLijstSimpleCursorAdapter.getItemId(nC);
    }


    private void addSomeData() {
        mDbAdapter.createlijst("Videos", true);
        mDbAdapter.createlijst("Stripverhalen", false);
        mDbAdapter.createlijst("adressen", false);
        mDbAdapter.createlijst("To do lijst", true);
        mDbAdapter.createlijst("Postzegels", false);
        mDbAdapter.createlijst("Bordspelen,", true);
        mDbAdapter.createlijst("Games", false);
        mDbAdapter.createlijst("Contacten", false);
    }

    private void fireCustomDialog(final Lijst lijst) {
// custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (lijst != null);
//this is for an edit
        if (isEditOperation) {
            titleView.setText("Edit Reminder");
            checkBox.setChecked(lijst.getBelangrijk() == 1);
            editCustom.setText(lijst.getNaam());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    Lijst reminderEdited = new Lijst(lijst.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateLijst(reminderEdited);
//this is for new reminder
                } else {
                    mDbAdapter.createlijst(reminderText, checkBox.isChecked());
                }
                mLijstSimpleCursorAdapter.changeCursor(mDbAdapter.fetchAllLijst());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new:
//create new Reminder
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }

    }
}
