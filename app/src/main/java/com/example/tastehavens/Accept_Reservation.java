package com.example.tastehavens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Accept_Reservation extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<ReservationDetails> reservationList = new ArrayList<>();
    List<ReservationDetails> fulldetails =  new ArrayList<>();
    Button accept,reject;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_reservation);


        recyclerView = findViewById(R.id.recycleViewer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ReservationDetails> reservationList = new ArrayList<>();
        ReservationAdapter adapter = new ReservationAdapter(this, reservationList);
        recyclerView.setAdapter(adapter);

        fetchData();




    }

    public void fetchData(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Reservations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reservationList.clear();

                    for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                        String reserveDate = document.getString("reservationDate");
                        String reserveTime = document.getString("reservationTime");
                        String reserveGuests = document.getString("guestCount");
                        String reserveCusName = document.getString("customerName");
                        String reserveTableNumber = document.getString("tableNumber");
                        String reserveNumber = document.getString("contactNumber");
                        String documentId = document.getId();  // Get the document ID

                        reservationList.add(new ReservationDetails(
                                reserveDate, reserveTime, reserveNumber,
                                reserveGuests, reserveCusName, reserveTableNumber,
                                documentId));  // Include documentId
                    }
                    fulldetails = new ArrayList<>(reservationList);
                    adapter = new ReservationAdapter(this, reservationList);
                    recyclerView.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load the items", Toast.LENGTH_SHORT).show();
                });
    }




}
