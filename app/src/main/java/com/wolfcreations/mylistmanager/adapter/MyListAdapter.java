package com.wolfcreations.mylistmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.wolfcreations.mylistmanager.R;
import com.wolfcreations.mylistmanager.model.MyList;
import java.util.ArrayList;


/**
 * Created by Gebruiker on 1/02/2016.
 */
public class MyListAdapter extends ArrayAdapter<MyList> {
    public final ArrayList<MyList> mValues;

    public MyListAdapter(Context context,  ArrayList<MyList> items) {
        super(context, 0, items);
        mValues = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MyList alist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.row_text);
        //TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(alist.getName());
        //tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }

     public MyList getItem(int position) {
        if (mValues != null)
            return mValues.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mValues != null)
            return mValues.get(position).getId();
        return 0;
    }
}
