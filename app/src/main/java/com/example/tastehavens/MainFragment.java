package com.example.tastehavens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {

    OrderDetailsAdapter adapter;

    List<orderDetails> orderList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainfragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.orders);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.profile) {
                    startActivity(new Intent(requireContext(), Profile.class));
                } else if (id == R.id.orders) {
                    startActivity(new Intent(requireContext(), orders_history.class));
                } else if (id == R.id.cart) {
                    startActivity(new Intent(requireContext(), Cart.class));
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(requireContext(), reserve.class));
                } else if (id == R.id.home) {
                    startActivity(new Intent(requireContext(), Menu.class));
                } else {
                    return false;
                }
                return true;
            }
        });


        adapter = new OrderDetailsAdapter(getContext(), orderList);
        recyclerView.setAdapter(adapter);

        String tabTitle = getArguments() != null ? getArguments().getString("tabTitle") : "pending";
        fetchOrdersByStatus(tabTitle.toLowerCase());

        return view;
    }


    private void fetchOrdersByStatus(String status) {
        db.collection("AllOrders")
                .whereEqualTo("status", status)
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
                            Log.e("MainFragment", "Error parsing order", e);
                        }
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("MainFragment", "Failed to fetch orders", e));
    }
}
