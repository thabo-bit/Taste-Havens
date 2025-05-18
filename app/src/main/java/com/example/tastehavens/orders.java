package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class orders extends AppCompatActivity {
    Button cart;
    ImageButton favvourites;
    TextView items, Meal, Discription, price;
    ImageView pic;
    ImageButton add, remove;
    FirebaseAuth auth;
    FirebaseFirestore database;

    int money;
    boolean isFavouritesSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        add = findViewById(R.id.add);
        items = findViewById(R.id.items);
        remove = findViewById(R.id.remove);
        pic = findViewById(R.id.pic);
        Meal = findViewById(R.id.Food_Type);
        Discription = findViewById(R.id.Discription);
        price = findViewById(R.id.textView5);
        cart = findViewById(R.id.Add);
        favvourites = findViewById(R.id.favourites);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("menuName");
        String menuPrice = getIntent().getStringExtra("menuPrice");
        String discription = getIntent().getStringExtra("discription");
        String image = getIntent().getStringExtra("menuImageUrl");

        Meal.setText(name);
        price.setText(menuPrice);
        Discription.setText(discription);
        Glide.with(this).load(image).into(pic);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = items.getText().toString();
                int increase = Integer.parseInt(value);
                increase += 1;
                items.setText(String.valueOf(increase));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = items.getText().toString();
                int increase = Integer.parseInt(value);

                if (increase >= 1) {
                    increase -= 1;
                } else {
                    return;
                }

                items.setText(String.valueOf(increase));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = items.getText().toString();
                int quantityInt = Integer.parseInt(quantity);

                String userId = auth.getCurrentUser().getUid();

                Cart_items cartitems = new Cart_items(name, menuPrice, quantityInt, image);

                database.collection("Users")
                        .document(userId)
                        .collection("cart")
                        .add(cartitems)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(orders.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(v.getContext(), Menu.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(orders.this, "Failed to add to cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                Intent intent = new Intent(v.getContext(), Cart.class);
                intent.putExtra("menuName", Meal.getText().toString());
                intent.putExtra("menuPrice", price.getText().toString());
                intent.putExtra("menuImageUrl", image);
                v.getContext().startActivity(intent);
            }
        });

        favvourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = items.getText().toString();
                int quantityInt = Integer.parseInt(quantity);
                Cart_items cartitems = new Cart_items(name, menuPrice, quantityInt, image);
                if (!isFavouritesSelected) {
                    favvourites.setImageResource(R.drawable.love);
                    isFavouritesSelected = true;
                } else {
                    favvourites.setImageResource(R.drawable.favourite);
                    isFavouritesSelected = false;
                }
                String ID = auth.getCurrentUser().getUid();
                database.collection("Users")
                        .document(ID)
                        .collection("Favourites")
                        .add(cartitems).addOnSuccessListener( documentReference -> {

                            Toast.makeText(orders.this, "Item has been added sucessfully", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e ->{

                            Toast.makeText(orders.this, "failed to get item", Toast.LENGTH_SHORT).show();

                        });



            }
        });


    }
}
