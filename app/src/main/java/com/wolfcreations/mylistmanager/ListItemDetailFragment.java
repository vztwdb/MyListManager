package com.wolfcreations.mylistmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wolfcreations.mylistmanager.adapter.TagSpinnerAdapter;
import com.wolfcreations.mylistmanager.dummy.DummyContent;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.TagEnum;
import com.wolfcreations.mylistmanager.model.ToDo;

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
public class ListItemDetailFragment extends android.support.v4.app.Fragment implements RatingBar.OnRatingBarChangeListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    public MyListItem mItem;

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
    static TextView ratingText;

    static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.listitem_detail, container, false);

        //View v = getView().findViewById(R.id.listitem_detail_container);
        Button addBtn = (Button) v.findViewById(R.id.addBtn);
        final EditText edtName = (EditText) v.findViewById(R.id.edtName);
        final EditText edtDescription = (EditText) v.findViewById(R.id.edtDescription);
        final EditText edtcomment = (EditText) v.findViewById(R.id.edtComment);
        final EditText edtUrl = (EditText) v.findViewById(R.id.edtUrl);
        final RatingBar edtRating = (RatingBar)v.findViewById(R.id.edtRating) ;
        ratingText = (TextView) v.findViewById(R.id.ratingText);
        edtRating.setOnRatingBarChangeListener(this);

        edtName.setText(mItem.getName());
        edtDescription.setText(mItem.getDescription());
        edtcomment.setText(mItem.getComment());
        edtUrl.setText(mItem.getUrl());
        edtRating.setRating (mItem.getRating());


        Spinner sp = (Spinner) v.findViewById(R.id.tagSpinner);
        TagSpinnerAdapter tsa = new TagSpinnerAdapter(getActivity(), tagList);
        sp.setAdapter(tsa);
        if ( (mItem.getId() != -1) || mItem.getPicture() != null )  selectSpinnerItemByValue(sp, mItem.getPicture());

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

        if (mItem.getCategory() == "Todo") {
            ToDo todo = (ToDo)mItem;
            selDate = todo.getDueDate();


            // We set the current date and time
            tvDate = (TextView) v.findViewById(R.id.inDate);
            tvTime = (TextView) v.findViewById(R.id.inTime);

            tvDate.setText(sdfDate.format(todo.getDueDate()));
            tvTime.setText(sdfTime.format(todo.getDueDate()));

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
                    tpf.show(getActivity().getFragmentManager(), "timepicker");

                }
            });
        }

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // We retrieve data inserted
                mItem.setName(edtName.getText().toString());
                mItem.setDescription(edtDescription.getText().toString());
                mItem.setComment(edtcomment.getText().toString());
                mItem.setUrl(edtUrl.getText().toString());
                mItem.setRating(edtRating.getRating());
                mItem.setPicture(currentTag);
                if (mItem.getCategory() == "Todo") {
                    ToDo todo = (ToDo)mItem;
                    todo.setDueDate(selDate) ;
                }
                // Safe cast
                ( (AddItemListener) getActivity()).onAddItem(mItem);
            }
        });

          return v;

    }

    public static void selectSpinnerItemByValue(Spinner spnr, TagEnum value) {
        if (value != null) {
            TagSpinnerAdapter adapter = (TagSpinnerAdapter) spnr.getAdapter();
            for (int position = 0; position < adapter.getCount(); position++) {
                if (adapter.getItem(position).getTagColor() == value.getTagColor()) {
                    spnr.setSelection(position);
                    return;
                }
            }
        }
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
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
        ratingText.setText(rating + "/" + numStars);
    }
}
