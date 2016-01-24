package com.wolfcreations.mylistmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gebruiker on 17/01/2016.
 */
public class TagSpinnerAdapter extends ArrayAdapter<TagEnum> {

    private Context ctx;
    private List<TagEnum> mTagMyList;

    public TagSpinnerAdapter(Context ctx, List<TagEnum> tagMyList) {
        super(ctx, R.layout.spinnertaglayout);
        this.ctx = ctx;
        this.mTagMyList = tagMyList;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return _getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return _getView(position, convertView, parent);
    }

    private View _getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            // Inflate spinner layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.spinnertaglayout, parent, false);
        }

        // We should use ViewHolder pattern
        ImageView iv = (ImageView) v.findViewById(R.id.tagSpinnerImage);
        TextView tv = (TextView) v.findViewById(R.id.tagNameSpinner);

        TagEnum t = mTagMyList.get(position);
        iv.setBackgroundResource(t.getTagColor());
        tv.setText(t.name());
        return v;
    }

}
