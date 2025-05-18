package com.example.tastehavens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Incoming_orders extends AppCompatActivity {

    RecyclerView recyclerView;
    order_Adapter_Class adapter;
    List<orderDetails> orderList;
    FirebaseFirestore db;
    LinearLayout empty;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_orders_1);

        recyclerView = findViewById(R.id.Viewer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        empty = findViewById(R.id.empty);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);

        orderList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        adapter = new order_Adapter_Class(this, orderList);
        recyclerView.setAdapter(adapter);

        fetchOrders();


        refreshLayout.setOnRefreshListener(() -> {
            fetchOrders();
        });
    }

    private void fetchOrders() {
        db.collection("AllOrders")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            String orderId = document.getString("orderId");
                            String timestamp = document.getString("timestamp");
                            String orderType = document.getString("orderType");
                            String customerName = document.getString("customerName");
                            String notes = document.getString("notes");
                            String userId = document.getString("userId");

                            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                            List<Cart_items> cartItems = new ArrayList<>();

                            if (items != null) {
                                for (Map<String, Object> item : items) {
                                    cartItems.add(new Cart_items(
                                            (String) item.get("mealName"),
                                            (String) item.get("price"),
                                            ((Long) item.get("quantity")).intValue(),
                                            (String) item.get("imageUrl")
                                    ));
                                }
                            }

                            orderList.add(new orderDetails(
                                    orderId,
                                    orderType,
                                    timestamp,
                                    customerName,
                                    cartItems,
                                    notes != null ? notes : "None",
                                    userId
                            ));
                        } catch (Exception e) {
                            Log.e("IncomingOrders", "Error parsing order", e);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false); // ✅ stop the refresh indicator
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("IncomingOrders", "Error loading orders", e);
                    refreshLayout.setRefreshing(false); // ✅ stop the refresh indicator on error
                });
    }

}