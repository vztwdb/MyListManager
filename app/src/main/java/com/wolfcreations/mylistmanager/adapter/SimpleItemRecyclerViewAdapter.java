package com.wolfcreations.mylistmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public SimpleItemRecyclerViewAdapter(Context ctx, List<MyListItem> items) {
        this.ctx = ctx;
        mValues = items;
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
                ctx.startActivity(intent);
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
