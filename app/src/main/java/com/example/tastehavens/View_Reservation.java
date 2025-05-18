package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class View_Reservation extends AppCompatActivity {

    TextView reason,customername,guest,time,tableNum,created,status;
    FirebaseFirestore database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reservation);


        customername = findViewById(R.id.tv_guest);
        guest = findViewById(R.id.tv_guests);
        time = findViewById(R.id.tv_date);
        tableNum = findViewById(R.id.tv_table);
        reason = findViewById(R.id.tv_reason);
        created = findViewById(R.id.created);
        status = findViewById(R.id.tv_status);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(View_Reservation.this, Profile.class));
                    return true;
                }
                if (id == R.id.profile) {
                    startActivity(new Intent(View_Reservation.this, Profile.class));
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(View_Reservation.this, orders_history.class));
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(View_Reservation.this, Cart.class));
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(View_Reservation.this, reserve.class));
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(View_Reservation.this, Menu.class));
                    return true;
                }

                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        String ID = auth.getCurrentUser().getUid();

        database.collection("Reservations")
                .whereEqualTo("userId", ID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                        customername.setText(doc.getString("customerName"));
                        time.setText(doc.getString("reservationDate"));
                        tableNum.setText(doc.getString("tableNumber"));
                        guest.setText(doc.getString("guestCount"));
                        reason.setText(doc.getString("reason"));
                        status.setText(doc.getString("acceptanceStatus"));

                        Timestamp timestamp = doc.getTimestamp("createdAt");
                        if (timestamp != null) {
                            Date date = timestamp.toDate();
                            DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy - HH:mm", Locale.getDefault());
                            String formattedDate = dateFormat.format(date);
                            created.setText(formattedDate);
                        } else {
                            created.setText("N/A");
                        }



                    } else {
                        reason.setText("No reservation found.");
                    }
                })
                .addOnFailureListener(e -> {
                    reason.setText("Error fetching reservation.");
                });




    }
}
