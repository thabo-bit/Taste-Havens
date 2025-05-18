package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    TextView greet;
    FirebaseUser data;
    ImageButton close;
    FirebaseAuth auth;

    CardView Reservation,log_out,favourites,update;
    FirebaseFirestore database;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Reservation = findViewById(R.id.cardView40);
        greet = findViewById(R.id.greetings);
        close = findViewById(R.id.imageButton7);
        log_out = findViewById(R.id.cardView7);
        favourites = findViewById(R.id.cardView2);
        update = findViewById(R.id.update);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();

        greet.setText(user.getDisplayName());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(Profile.this, Profile.class));
                    return true;
                }
                if (id == R.id.profile) {
                    startActivity(new Intent(Profile.this, Profile.class));
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(Profile.this, orders_history.class));
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(Profile.this, Cart.class));
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(Profile.this, reserve.class));
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(Profile.this, Menu.class));
                    return true;
                }

                return false;
            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Profile.this, com.example.tastehavens.Menu.class);
                 startActivity(intent);
            }
        });


        Reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this,View_Reservation.class);
                startActivity(intent);

            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, favourites.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intant = new Intent(Profile.this,Update_details.class);
                startActivity(intant);
                finish();
            }
        });

    }



}