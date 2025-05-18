package com.example.tastehavens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    List<Cart_items> list;
    Context context;

    public FavouritesAdapter(List<Cart_items> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.menu_looks, parent, false); // use your XML filename here
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart_items item = list.get(position);
        holder.meal.setText(item.getMealName());
        holder.price.setText("R" + item.getPrice());

        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.menuImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton menuImage;
        TextView meal, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.Menu);
            meal = itemView.findViewById(R.id.Meal);
            price = itemView.findViewById(R.id.Price);
        }
    }
}
