package com.example.chamster.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chamster.R;
import com.example.chamster.model.HamsterItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HamsterAdapter extends RecyclerView.Adapter<HamsterAdapter.ViewHolder> {

    private List<HamsterItem> items;
    private Context context;

    public HamsterAdapter(Context context, List<HamsterItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HamsterItem item = items.get(position);

        holder.txtName.setText(item.getName());
        holder.txtDesc.setText(item.getDescription());

        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(item.getImagePath());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.imgAnimal.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HamsterAdapter.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("desc", item.getDescription());
            intent.putExtra("img", item.getImagePath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnimal;
        TextView txtName, txtDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnimal = itemView.findViewById(R.id.imgAnimal);
            txtName = itemView.findViewById(R.id.txtName);
            txtDesc = itemView.findViewById(R.id.txtDesc);
        }
    }
}
