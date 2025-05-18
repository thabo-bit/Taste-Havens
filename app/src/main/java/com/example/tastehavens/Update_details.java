package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Update_details  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_details);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(Update_details.this, Profile.class));
                    finish();
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(Update_details.this, orders_history.class));
                    finish();
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(Update_details.this, Cart.class));
                    finish();
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(Update_details.this, Incoming_orders.class));
                    finish();
                    return true;
                }

                return false;
            }
        });



    }

}
