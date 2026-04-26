package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chamster.R;
import com.example.chamster.data.model.HamsterItem;

import java.io.InputStream;
import java.util.List;

public class HamsterAdapter extends RecyclerView.Adapter<HamsterAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(HamsterItem item);
    }

    private final List<HamsterItem> items;
    private final OnItemClickListener listener;

    public HamsterAdapter(List<HamsterItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hamster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HamsterItem item = items.get(position);
        holder.title.setText(item.getName());

        try {
            InputStream is = holder.itemView.getContext()
                    .getAssets()
                    .open(item.getImagePath());
            Drawable d = Drawable.createFromStream(is, null);
            holder.image.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.itemTitle);
        }
    }
}