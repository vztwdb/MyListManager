package com.wolfcreations.mylistmanager;

import android.widget.ArrayAdapter;
import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemAdapter extends ArrayAdapter<MyListItem> {

    private Context ctx;
    private List<MyListItem> itemList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public MyListItem getItem(int position) {
        if (itemList != null)
            return itemList.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();

        return 0;
    }



    @Override
    public int getCount() {
        if (itemList != null)
            return itemList.size();

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemHolder h = null;

        if (v == null) {
            // Inflate row layout
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.listitem_detail, parent, false);

            // Look for Views in the layout
            ImageView iv = (ImageView) v.findViewById(R.id.tagView);
            TextView nameTv = (TextView) v.findViewById(R.id.nameView);
            TextView descrView = (TextView) v.findViewById(R.id.descrView);
            TextView dateView = (TextView) v.findViewById(R.id.dateView);

            h = new ItemHolder();
            h.tagView = iv;
            h.nameView = nameTv;
            h.descrView = descrView;
            h.dateView = dateView;

            v.setTag(h);
        }
        else
            h = (ItemHolder) v.getTag();

        h.nameView.setText(itemList.get(position).getName());
        h.descrView.setText(itemList.get(position).getComment());
        h.tagView.setBackgroundResource(itemList.get(position).myTag.getTagColor());
        h.dateView.setText(sdf.format(itemList.get(position).duedate));

        return v;
    }

    public ListItemAdapter(Context context, List<MyListItem> itemList) {
        super(context, R.layout.listitem_detail);
        this.ctx = context;
        this.itemList = itemList;
    }

    // ViewHolder pattern
    static class ItemHolder {
        ImageView tagView;
        TextView nameView;
        TextView descrView;
        TextView dateView;
    }

}
