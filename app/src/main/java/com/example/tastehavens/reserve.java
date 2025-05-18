package com.example.tastehavens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class reserve extends AppCompatActivity {

    EditText date, time, CusName, CusNumber, NumGuest;
    int hour, Minutes;
    Button reserveButton;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseUser FirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_reserve);

        date = findViewById(R.id.editTextDate);
        time = findViewById(R.id.editTextTime);
        reserveButton = findViewById(R.id.buttonReserve);
        CusName = findViewById(R.id.editTextName);
        CusNumber = findViewById(R.id.editTextPhone);
        NumGuest = findViewById(R.id.editTextGuests);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.reservation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(reserve.this, Profile.class));
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(reserve.this, orders_history.class));
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(reserve.this, Cart.class));
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(reserve.this, reserve.class));
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(reserve.this, Menu.class));
                    return true;
                }
                return false;
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        reserve.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthName = new DateFormatSymbols().getMonths()[monthOfYear];
                                date.setText(dayOfMonth + " " + monthName + " " + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        Minutes = minute;
                        time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        reserve.this,
                        onTimeSetListener,
                        hour,
                        Minutes,
                        true
                );
                timePickerDialog.show();
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser == null) {
                        Toast.makeText(reserve.this, "Please login to make a reservation", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = currentUser.getUid();

                    database.collection("Reservations")
                            .whereEqualTo("userId", userId)
                            .whereIn("acceptanceStatus", Arrays.asList("Pending", "Confirmed"))
                            .get()
                            .addOnCompleteListener(checkTask -> {
                                if (checkTask.isSuccessful()) {
                                    if (!checkTask.getResult().isEmpty()) {
                                        Toast.makeText(reserve.this,
                                                "You already have an active reservation. Please wait for it to be processed.",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        proceedWithReservation(userId);
                                    }
                                } else {
                                    Toast.makeText(reserve.this,
                                            "Error checking existing reservations: " + checkTask.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    Log.e("ReservationError", "Error in reservation", e);
                    Toast.makeText(reserve.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceedWithReservation(String userId) {
        String reserveDate = date.getText().toString().trim();
        String reserveTime = time.getText().toString().trim();
        String reserveNumber = CusNumber.getText().toString().trim();
        String reserveCusName = CusName.getText().toString().trim();
        String reserveGuest = NumGuest.getText().toString().trim();

        if (reserveDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            date.requestFocus();
            return;
        }

        if (reserveTime.isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            time.requestFocus();
            return;
        }

        if (reserveCusName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            CusName.requestFocus();
            return;
        }

        if (reserveNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            CusNumber.requestFocus();
            return;
        }

        if (reserveGuest.isEmpty()) {
            Toast.makeText(this, "Please enter number of guests", Toast.LENGTH_SHORT).show();
            NumGuest.requestFocus();
            return;
        }

        if (reserveNumber.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            CusNumber.requestFocus();
            return;
        }

        try {
            int guestCount = Integer.parseInt(reserveGuest);
            if (guestCount <= 0) {
                Toast.makeText(this, "Number of guests must be at least 1", Toast.LENGTH_SHORT).show();
                NumGuest.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for guests", Toast.LENGTH_SHORT).show();
            NumGuest.requestFocus();
            return;
        }

        Map<String, Object> reservationData = new HashMap<>();
        reservationData.put("userId", userId);
        reservationData.put("customerName", reserveCusName);
        reservationData.put("contactNumber", reserveNumber);
        reservationData.put("reservationDate", reserveDate);
        reservationData.put("reservationTime", reserveTime);
        reservationData.put("guestCount", reserveGuest);
        reservationData.put("tableNumber", "N/A");
        reservationData.put("acceptanceStatus", "Pending");
        reservationData.put("reason", "");
        reservationData.put("createdAt", FieldValue.serverTimestamp());

        database.collection("Reservations")
                .add(reservationData)
                .addOnSuccessListener(documentReference -> {
                    database.collection("analytics")
                            .document("Stats")
                            .update("reservations", FieldValue.increment(1))
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(reserve.this, "Reservation successful!", Toast.LENGTH_SHORT).show();
                                date.setText("");
                                time.setText("");
                                CusName.setText("");
                                CusNumber.setText("");
                                NumGuest.setText("");
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(reserve.this,
                                        "Reservation saved but analytics update failed",
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(reserve.this,
                            "Reservation failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}