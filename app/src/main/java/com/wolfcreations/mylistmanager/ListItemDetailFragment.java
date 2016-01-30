package com.wolfcreations.mylistmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wolfcreations.mylistmanager.adapter.TagSpinnerAdapter;
import com.wolfcreations.mylistmanager.dummy.DummyContent;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.TagEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a single MyListItem detail screen.
 * This fragment is either contained in a {@link ListItemActivity}
 * in two-pane mode (on tablets) or a {@link ListItemDetailActivity}
 * on handsets.
 */
public class ListItemDetailFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private MyListItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListItemDetailFragment() {
    }

    private static List<TagEnum> tagList = new ArrayList<TagEnum>();
    static {
        tagList.add(TagEnum.BLACK);
        tagList.add(TagEnum.BLUE);
        tagList.add(TagEnum.GREEN);
        tagList.add(TagEnum.RED);
        tagList.add(TagEnum.YELLOW);
    }

    private TagEnum currentTag;
    private static Date selDate = new Date();

    static TextView tvDate;
    static TextView tvTime;

    static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String id = getArguments().getString(ARG_ITEM_ID);
            mItem = DummyContent.ITEM_MAP.get(id);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.listitem_detail, container, false);

        //View v = getView().findViewById(R.id.listitem_detail_container);
        Button addBtn = (Button) v.findViewById(R.id.addBtn);
        final EditText edtName = (EditText) v.findViewById(R.id.edtName);
        final EditText edtDescr = (EditText) v.findViewById(R.id.edtDescr);
        final EditText edtNote = (EditText) v.findViewById(R.id.edtNote);

        edtName.setText(mItem.getName());
        edtDescr.setText(mItem.getComment());
        edtNote.setText(mItem.getUrl());
        selDate = mItem.duedate;

        Spinner sp = (Spinner) v.findViewById(R.id.tagSpinner);
        TagSpinnerAdapter tsa = new TagSpinnerAdapter(getActivity(), tagList);
        sp.setAdapter(tsa);
        sp.setSelection(2);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adptView, View view,
                                       int pos, long id) {

                currentTag = (TagEnum) adptView.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // We retrieve data inserted
                mItem.setName(edtName.getText().toString());
                mItem.setComment(edtDescr.getText().toString());
                mItem.setUrl(edtNote.getText().toString());
                mItem.myTag= currentTag;
                mItem.duedate = selDate ;
                // Safe cast
                ( (AddItemListener) getActivity()).onAddItem(mItem);
            }
        });

        // We set the current date and time
        tvDate = (TextView) v.findViewById(R.id.inDate);
        tvTime = (TextView) v.findViewById(R.id.inTime);

        tvDate.setText(sdfDate.format(mItem.duedate));
        tvTime.setText(sdfTime.format(mItem.duedate));

        tvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment();
                dpf.show(getActivity().getFragmentManager(), "datepicker");

            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerFragment tpf = new TimePickerFragment();
                tpf.show(getActivity().getFragmentManager(), "timepicker") ;

            }
        });

        return v;

    }

    public interface AddItemListener {
         void onAddItem(MyListItem item);
    }


    // We create a Dialog fragment holding Date picker
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            c.setTime(selDate);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth, 9, 0);
            selDate = c.getTime();


            tvDate.setText(sdfDate.format(selDate));
        }

    }

    // We create a Dialog fragment holding Date picker
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            c.setTime(selDate);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));


        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            c.setTime(selDate);
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            selDate = c.getTime();

            // We set the hour
            tvTime.setText(sdfTime.format(selDate));
        }

    }
}
