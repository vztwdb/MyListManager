package com.wolfcreations.mylistmanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wolfcreations.mylistmanager.ListActivity;
import com.wolfcreations.mylistmanager.ListItemActivity;
import com.wolfcreations.mylistmanager.ListItemDetailActivity;
import com.wolfcreations.mylistmanager.R;
import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.ToDo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Gebruiker on 1/02/2016.
 */
public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    public final List<MyListItem> mValues;
    private Context ctx;
    private ListDbAdapter mDbAdapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public SimpleItemRecyclerViewAdapter(Context ctx, List<MyListItem> items, ListDbAdapter db) {
        this.ctx = ctx;
        mValues = items;
        mDbAdapter = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_row, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.currentPosition = position;
        holder.nameView.setText(holder.mItem.getName());
        holder.descrView.setText(holder.mItem.getComment());
        if (holder.mItem.getPicture() != null)   holder.tagView.setBackgroundResource(holder.mItem.getPicture().getTagColor());

        if (holder.mItem.getCategory() == "Todo" ){
            ToDo todo = (ToDo) holder.mItem;
            holder.dateView.setText(sdf.format(todo.getDueDate()));
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ListItemDetailActivity.class);
                ListItemDetailActivity.CurrentListItem = holder.mItem;
                ((Activity)ctx).startActivityForResult(intent, ListItemActivity.ADD_ITEM);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                ListView modeListView = new ListView(ctx);
                String[] modes = new String[]{"Delete listitem"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(ctx,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            mDbAdapter.deleteListItemById(holder.mItem.getId());
                            mValues.remove(holder.mItem);
                            notifyItemRemoved(holder.currentPosition);
                        }

                        dialog.dismiss();
                    }
                });
                return true;
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
        ImageView tagView;
        TextView nameView;
        TextView descrView;
        TextView dateView;
        TextView autorView;
        TextView producerView;
        public MyListItem mItem;
        int currentPosition = 0;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // Look for Views in the layout
            tagView = (ImageView) view.findViewById(R.id.tagView);
            nameView = (TextView) view.findViewById(R.id.nameView);
            descrView = (TextView) view.findViewById(R.id.descrView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            autorView= (TextView) view.findViewById(R.id.autorView);
            producerView = (TextView) view.findViewById(R.id.producerView);
            view.setTag(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
