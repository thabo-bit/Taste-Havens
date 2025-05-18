package com.example.tastehavens;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastehavens.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetails";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_orders);

        db = FirebaseFirestore.getInstance();
        String orderId = getIntent().getStringExtra("orderId");

        if (orderId == null) {
            Toast.makeText(this, "Order ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchOrderDetails(orderId);
    }

    private void fetchOrderDetails(String orderId) {
        // Try AllOrders collection first
        db.collection("AllOrders").document(orderId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        displayOrder(document);
                    } else {
                        // Fallback to user-specific orders
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("Users").document(userId)
                                .collection("UserOrders").document(orderId)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    if (userDoc.exists()) {
                                        displayOrder(userDoc);
                                    } else {
                                        showOrderNotFound();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching order", e);
                    Toast.makeText(this, "Error loading order", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayOrder(DocumentSnapshot document) {
        try {
            TextView orderNumber = findViewById(R.id.orderNumber);
            TextView timeReceived = findViewById(R.id.timeReceived);
            TextView customerName = findViewById(R.id.customerName);
            TextView tableDelivery = findViewById(R.id.tableDelivery);
            TextView orderItems = findViewById(R.id.order_items);
            TextView specialNotes = findViewById(R.id.specialNotes);

            // Set basic order info
            orderNumber.setText("Order #" + document.getString("orderId"));
            timeReceived.setText("Time: " + document.getString("timestamp"));
            customerName.setText("Customer: " + document.getString("customerName"));
            tableDelivery.setText("Type: " + document.getString("orderType"));
            specialNotes.setText("Notes: " + document.getString("notes"));

            // Build items list
            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
            StringBuilder itemsText = new StringBuilder();
            if (items != null) {
                for (Map<String, Object> item : items) {
                    itemsText.append("• ")
                            .append(item.get("mealName"))
                            .append(" x")
                            .append(item.get("quantity"))
                            .append(" (R")
                            .append(item.get("price"))
                            .append(")\n");
                }
            } else {
                itemsText.append("No items found");
            }
            orderItems.setText(itemsText.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error displaying order", e);
            Toast.makeText(this, "Error displaying order details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showOrderNotFound() {
        Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
        finish();
    }
}
