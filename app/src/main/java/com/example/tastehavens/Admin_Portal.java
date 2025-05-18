package com.example.tastehavens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class Admin_Portal extends AppCompatActivity {

    PieChart pie;
    TextView Revenuedata,complete,reservation,salaries;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    CardView addUser,AcceptReservation,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_portal);


        pie = findViewById(R.id.piechart);
        Revenuedata = findViewById(R.id.revenue);
        complete = findViewById(R.id.orders_completed);
        addUser = findViewById(R.id.add_user);
        reservation = findViewById(R.id.reservations);
        AcceptReservation = findViewById(R.id.accept_Reservation);
        salaries = findViewById(R.id.Salaries);
        user = findViewById(R.id.ViewUser);


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Portal.this, View_User.class);
                startActivity(intent);
            }
        });





         database.collection("analytics")
                         .document("Stats")
                                 .get().addOnSuccessListener(documentSnapshot -> {

                                     Long revenue = documentSnapshot.getLong("Revenue");
                                     String money = String.valueOf(revenue);

                                     Revenuedata.setText("R" + money);
                 }).addOnFailureListener(e -> {});

        database.collection("analytics")
                .document("Stats")
                .get().addOnSuccessListener(documentSnapshot -> {

                    Long completeddata = documentSnapshot.getLong("OrdersCompleted");
                    String completed = String.valueOf(completeddata);
                    complete.setText(completed);

                }).addOnFailureListener(e -> {

                });

        database.collection("analytics")
                .document("Stats")
                .get().addOnSuccessListener(documentSnapshot -> {
                    Long reservationdata =  documentSnapshot.getLong("reservations");

                    String Reservation = String.valueOf(reservationdata);
                    reservation.setText(Reservation);

                }).addOnFailureListener(e -> {

                });

        database.collection("analytics")
                .document("Stats")
                .get().addOnSuccessListener(documentSnapshot -> {
                    Long salary =  documentSnapshot.getLong("salaries");

                    String allsalary = String.valueOf(salary);
                    salaries.setText(allsalary);

                }).addOnFailureListener(e -> {

                });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Portal.this,Add_Users.class);
                startActivity(intent);
            }
        });

        AcceptReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Portal.this,Accept_Reservation.class);
                startActivity(intent);
            }
        });

        String sal = salaries.getText().toString();

        int loss = Integer.parseInt(sal);

        String money = Revenuedata.getText().toString();

        int profit = Integer.parseInt(money);

        PieModel p1 = new PieModel("Loss",320 , Color.parseColor("#fc3517"));
        PieModel p2 = new PieModel("Profit", 500, Color.parseColor("#22fc17"));

        pie.addPieSlice(p1);
        pie.addPieSlice(p2);


        pie.startAnimation();
    }


}