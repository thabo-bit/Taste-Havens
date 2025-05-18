package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class favourites extends AppCompatActivity {
    RecyclerView recyclerView;
    FavouritesAdapter adapter;
    List<Cart_items> favouritesList = new ArrayList<>();  // Use Cart_items, not MenuItems

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(favourites.this, 2));
        adapter = new FavouritesAdapter(favouritesList);
        recyclerView.setAdapter(adapter);
        loadFavourites();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(favourites.this, Profile.class));
                    return true;
                }
                if (id == R.id.profile) {
                    startActivity(new Intent(favourites.this, Profile.class));
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(favourites.this, orders_history.class));
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(favourites.this, Cart.class));
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(favourites.this, reserve.class));
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(favourites.this, Menu.class));
                    return true;
                }

                return false;
            }
        });
    }

    private void loadFavourites() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String ID = auth.getCurrentUser().getUid();

        firestore.collection("Users")
                .document(ID)
                .collection("Favourites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favouritesList.clear();
                    for (var document : queryDocumentSnapshots.getDocuments()) {
                        String name = document.getString("mealName");
                        String price = document.getString("price");  // assuming price was stored as String
                        Long quantity = document.getLong("quantity");
                        String imageUrl = document.getString("imageUrl");

                        if (name != null && price != null && quantity != null && imageUrl != null) {
                            Cart_items item = new Cart_items(name, price, quantity.intValue(), imageUrl);
                            favouritesList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(favourites.this, "Failed to load favourites", Toast.LENGTH_SHORT).show();
                });
    }
}
