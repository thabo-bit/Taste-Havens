package com.example.tastehavens;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.bumptech.glide.Glide;


public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.MyViewHolder> {

    private List<MenuItems> Menu;

    public Items_Adapter(List<MenuItems> menuItemsList) {
        this.Menu = menuItemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_looks, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MenuItems item = Menu.get(position);

        holder.menuName.setText(item.getMenuName());
        holder.menuPrice.setText(item.getPrice());


        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.menuImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Clicked: " + item.getMenuName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(),orders.class);
               intent.putExtra("menuName",item.getMenuName());
              intent.putExtra("menuPrice",item.getPrice());
             intent.putExtra("menuImageUrl",item.getImageUrl());
             intent.putExtra("discription",item.getDiscription());
                v.getContext().startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return Menu.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView menuName, menuPrice;
        ImageView menuImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.Meal);
            menuPrice = itemView.findViewById(R.id.Price);
            menuImage = itemView.findViewById(R.id.Menu);
        }
    }
}
