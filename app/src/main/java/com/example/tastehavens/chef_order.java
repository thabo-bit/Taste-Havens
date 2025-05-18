package com.example.tastehavens;

import android.os.Bundle;
import android.util.Log; // Import Log
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot; // Import QueryDocumentSnapshot

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class chef_order extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextView orderitems, customer, time, notes, ordernumber, ordertype;
    Button reject, accept,preparing,ready,AlmostReady,click;
    LinearLayout buttons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_orders);

        orderitems = findViewById(R.id.order_items);
        customer = findViewById(R.id.customerName);
        time = findViewById(R.id.timeReceived);
        notes = findViewById(R.id.specialNotes);
        ordernumber = findViewById(R.id.orderNumber);
        ordertype = findViewById(R.id.tableDelivery);
        accept = findViewById(R.id.btnAccept);
        reject = findViewById(R.id.btnReject);
        preparing = findViewById(R.id.btnPreparing);
        ready = findViewById(R.id.btnReady);
        AlmostReady = findViewById(R.id.btnAlmostReady);
        buttons = findViewById(R.id.preparationButtonsLayout);


        ArrayList<get_Meals> meals = new ArrayList<get_Meals>();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user != null) {
            String userId = auth.getCurrentUser().getUid();

            database.collection("Users")
                    .document(userId)
                    .collection("ordered")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        StringBuilder mealNames = new StringBuilder();
                        for (var doc : queryDocumentSnapshots) {
                            List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");
                            if (items != null) {
                                for (Map<String, Object> item : items) {
                                    String mealName = (String) item.get("mealName");
                                    Long quantity = (Long) item.get("quantity");


                                    mealNames.append("• ").append(mealName);
                                    if (quantity != null) {
                                        mealNames.append(" x").append(quantity);
                                    }
                                    mealNames.append("\n");
                                }
                            }
                        }
                        orderitems.setText(mealNames.toString());
                    })

                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Failed to retrieve orders: " + e.getMessage());
                    });

            database.collection("Users")
                    .document(userId)
                    .collection("ordered")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for(var document : queryDocumentSnapshots){

                            String Customername = document.getString("customerName");
                            String orderId =  document.getString("orderId");
                            String SpecialNotes = document.getString("specialNotes");
                            String orderTime = document.getString("timestamp");
                            String  OrderType = document.getString("orderType");

                            customer.setText("Customer Name: "+Customername);
                            time.setText("Order Time: "+orderTime);
                            notes.setText("Special notes: "+SpecialNotes);
                            ordernumber.setText("#"+orderId);
                            ordertype.setText(OrderType);


                        }
                    }).addOnFailureListener(e -> {

                        Toast.makeText(this, "Load Up Failed", Toast.LENGTH_SHORT).show();

                    });
        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                ready.setVisibility(View.VISIBLE);
                preparing.setVisibility(View.VISIBLE);
                AlmostReady.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.VISIBLE);
            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(chef_order.this, "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
