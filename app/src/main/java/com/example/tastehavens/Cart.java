package com.example.tastehavens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Cart_Adapter cartAdapter;
    private List<Cart_items> cartItems;
    private Button totalButton,back;
    private View cart_remove;
    private TextView balance;

    private LinearLayout text,empty,infor;
    ConstraintLayout layout ;

    private double totalAmount = 0.0;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.cart);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(Cart.this, Profile.class));
                    finish();
                    return true;
                }
                if (id == R.id.profile) {
                    startActivity(new Intent(Cart.this, Profile.class));
                    finish();
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(Cart.this, orders_history.class));
                    finish();
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(Cart.this, Cart.class));
                    finish();
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(Cart.this, reserve.class));
                    finish();
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(Cart.this, Menu.class)

                    );finish();
                    return true;
                }

                return false;
            }
        });



        balance = findViewById(R.id.textView9);
        layout = findViewById(R.id.background);
        empty = findViewById(R.id.empty);
        infor = findViewById(R.id.linearLayout10);
        back = findViewById(R.id.backToMenu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, Menu.class);
                startActivity(intent);
            }
        });






        initializeViews();
        setupRecyclerView();
        setupSwipeToDelete();
        fetchCartItems();
        updateEmptyViewVisibility();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initializeViews();
            setupRecyclerView();
            setupSwipeToDelete();
            fetchCartItems();
            updateEmptyViewVisibility();

            swipeRefreshLayout.setRefreshing(false);
        });



}


    private void initializeViews() {
        recyclerView = findViewById(R.id.Item_remove);
        totalButton = findViewById(R.id.button);
        cart_remove = findViewById(R.id.Item_remove);
//        balance = findViewById(R.id.Balance);
        text = findViewById(R.id.linearLayout10);


        totalButton.setOnClickListener(v -> showBottomSheet());
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        cartAdapter = new Cart_Adapter(this, cartItems, new Cart_Adapter.OnItemClickListener() {

            @Override
            public void onAddClick(int position) {
                updateItemQuantity(position, 1);
            }

            @Override
            public void onRemoveClick(int position) {
                updateItemQuantity(position, -1);
            }

            @Override
            public void onQuantityChanged(int position, int newQuantity) {
                // Implement if needed
            }

        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
    }

    private void updateItemQuantity(int position, int change) {
        Cart_items item = cartItems.get(position);
        int newQuantity = item.getQuantity() + change;

        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            updateCartItemInFirestore(item);
            cartAdapter.notifyItemChanged(position);
            calculateTotal();
        }
    }

    private void updateCartItemInFirestore(Cart_items item) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("Users")
                .document(userId)
                .collection("cart")
                .document(item.getDocumentId())
                .update("quantity", item.getQuantity())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update quantity", Toast.LENGTH_SHORT).show());
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Cart_items deletedItem = cartItems.get(position);
                deleteCartItem(deletedItem, position);


            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    private void deleteCartItem(Cart_items item, int position) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("Users")
                .document(userId)
                .collection("cart")
                .document(item.getDocumentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    cartItems.remove(position);
                    cartAdapter.notifyItemRemoved(position);
                    calculateTotal();
                    showUndoSnackbar(item, position);
                    updateEmptyViewVisibility();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    cartAdapter.notifyItemChanged(position);
                });



    }

    private void showUndoSnackbar(Cart_items item, int position) {
        Snackbar.make(cart_remove, item.getMealName() + " removed", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    cartItems.add(position, item);
                    cartAdapter.notifyItemInserted(position);
                    calculateTotal();
                    restoreCartItem(item);
                }).show();
    }

    private void restoreCartItem(Cart_items item) {
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("mealName", item.getMealName());
        cartItem.put("price", item.getPrice());
        cartItem.put("quantity", item.getQuantity());
        cartItem.put("imageUrl", item.getImageUrl());

        db.collection("Users")
                .document(userId)
                .collection("cart")
                .document(item.getDocumentId())
                .set(cartItem)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to restore item", Toast.LENGTH_SHORT).show());

        updateEmptyViewVisibility();


    }

    private double calculateTotal() {
        totalAmount = 0.0;
        for (Cart_items item : cartItems) {
            try {
                double price = Double.parseDouble(item.getPrice().replace("R", ""));
                totalAmount += price * item.getQuantity();
            } catch (NumberFormatException e) {
                Log.e("Cart", "Error parsing price", e);
            }
        }


        totalButton.setText(String.format("Total R%.2f", totalAmount));
        return totalAmount;


    }

    @SuppressLint("NewApi")
    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.payment_method, null);

        if (bottomSheetView == null) {
            Toast.makeText(this, "Error loading payment view", Toast.LENGTH_SHORT).show();
            return;
        }

        bottomSheetDialog.setContentView(bottomSheetView);

        EditText cardNumber = bottomSheetView.findViewById(R.id.CardNumber);
        EditText expiryDate = bottomSheetView.findViewById(R.id.ExpiryDate);
        EditText cvc = bottomSheetView.findViewById(R.id.TextCVC);
        EditText nameOnCard = bottomSheetView.findViewById(R.id.CardName);
        Button payButton = bottomSheetView.findViewById(R.id.paying);
        CheckBox checkBox = bottomSheetView.findViewById(R.id.checkbox);
        RadioGroup orderTypeRadioGroup = bottomSheetView.findViewById(R.id.orderTypeRadioGroup);
        EditText specialNotes = bottomSheetView.findViewById(R.id.noteEditText);
        TextView balanceTextView = bottomSheetView.findViewById(R.id.textView10);

        String userId = auth.getCurrentUser().getUid();


        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double userBalance = documentSnapshot.getDouble("amount");
                        if (userBalance != null) {
                            balanceTextView.setText(String.format("Balance: R%.2f", userBalance));
                        }
                    }
                });




        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cardNumber.setEnabled(!isChecked);
            expiryDate.setEnabled(!isChecked);
            cvc.setEnabled(!isChecked);
            nameOnCard.setEnabled(!isChecked);


            int textColor = isChecked ? Color.GRAY : Color.BLACK;
            cardNumber.setTextColor(textColor);
            expiryDate.setTextColor(textColor);
            cvc.setTextColor(textColor);
              nameOnCard.setTextColor(textColor);
        });

        // Set up pay button click listener
        payButton.setOnClickListener(v -> {
            // Validate payment method selection
            int selectedOrderTypeId = orderTypeRadioGroup.getCheckedRadioButtonId();
            if (selectedOrderTypeId == -1) {
                Toast.makeText(this, "Please select order type (Dine-in or Collection)", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedOrderType = bottomSheetView.findViewById(selectedOrderTypeId);
            boolean isDineIn = selectedOrderType.getText().toString().equalsIgnoreCase("Dine-in");


            if (!checkBox.isChecked()) {

                if (TextUtils.isEmpty(cardNumber.getText().toString()) ||
                        TextUtils.isEmpty(expiryDate.getText().toString()) ||
                        TextUtils.isEmpty(cvc.getText().toString()) ||
                        TextUtils.isEmpty(nameOnCard.getText().toString())) {
                    Toast.makeText(this, "Please fill all card details", Toast.LENGTH_SHORT).show();
                    return;
                }


            }

            // Get special notes
            String notes = specialNotes.getText().toString();

            // Process payment
            processPayment(isDineIn, notes, checkBox.isChecked(), bottomSheetDialog);
        });

        bottomSheetDialog.show();
    }

    private void processPayment(boolean isDineIn, String notes, boolean isCashPayment, BottomSheetDialog dialog) {
        String userId = auth.getCurrentUser().getUid();

        // First check user balance
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double userBalance = documentSnapshot.getDouble("amount");
                        if (userBalance != null) {
                            double remainingBalance = userBalance - totalAmount;

                            if (remainingBalance >= 0) {
                                placeOrder(isDineIn, notes, remainingBalance);

                                updateRevenue(totalAmount);


                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to check balance", Toast.LENGTH_SHORT).show();
                });

    }

    private void placeOrder(boolean isDineIn, String notes, double remainingBalance) {
        String userId = auth.getCurrentUser().getUid();
        String customerName = auth.getCurrentUser().getDisplayName();
        if (customerName == null || customerName.isEmpty()) {
            customerName = "Customer";
        }









        int table = (int) (Math.random() * 40 + 1);
        String orderId = "ORD-" + System.currentTimeMillis();
        String orderType = isDineIn ? "Table: " + table : "Collection";
        String timestamp = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                .format(new Date());

        List<Map<String, Object>> orderItems = new ArrayList<>();
        for (Cart_items item : cartItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("mealName", item.getMealName());
            itemMap.put("price", item.getPrice());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("imageUrl", item.getImageUrl());
            orderItems.add(itemMap);
        }

        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("customerName", customerName);
        order.put("orderType", orderType);
        order.put("notes", TextUtils.isEmpty(notes) ? "None" : notes);
        order.put("orderId", orderId);
        order.put("items", orderItems);
        order.put("timestamp", timestamp);
        order.put("status", "pending");
        order.put("totalAmount", totalAmount);
        order.put("paymentMethod", "Cash on " + (isDineIn ? "arrival" : "collection"));









        db.collection("Users")
                .document(userId)
                .update("amount", remainingBalance)
                .addOnSuccessListener(aVoid -> {
                    // Then create the order
                    db.collection("AllOrders")
                            .document(orderId)
                            .set(order)
                            .addOnSuccessListener(aVoid1 -> {
                                db.collection("Users")
                                        .document(userId)
                                        .collection("UserOrders")
                                        .document(orderId)
                                        .set(order)
                                        .addOnSuccessListener(aVoid2 -> {
                                            Toast.makeText(this, "Order Successful", Toast.LENGTH_SHORT).show();
                                            clearCart(userId);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Order placed but history not saved", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Order Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update balance", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearCart(String userId) {
        WriteBatch batch = db.batch();

        db.collection("Users")
                .document(userId)
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        batch.delete(document.getReference());
                    }

                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                cartItems.clear();
                                cartAdapter.notifyDataSetChanged();
                                calculateTotal();
                                updateEmptyViewVisibility();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to clear cart", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get cart items", Toast.LENGTH_SHORT).show();
                });


    }

    private void fetchCartItems() {
        String userId = auth.getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("Users")
                .document(userId)
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartItems.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String mealName = document.getString("mealName");
                        String price = document.getString("price");
                        Long quantity = document.getLong("quantity");
                        String imageUrl = document.getString("imageUrl");

                        if (mealName != null && price != null) {
                            Cart_items item = new Cart_items(
                                    mealName,
                                    price,
                                    quantity != null ? quantity.intValue() : 1,
                                    imageUrl
                            );
                            item.setDocumentId(document.getId());
                            cartItems.add(item);
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                    calculateTotal();
                    updateEmptyViewVisibility();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                });


    }

    public void updateRevenue(double Amount){

     FirebaseFirestore database = FirebaseFirestore.getInstance();

     database.collection("analytics")
             .document("Stats")
             .update("Revenue" , FieldValue.increment(Amount))
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void unused) {

                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {

                 }
             });


    }

    private void updateEmptyViewVisibility() {
        if (cartItems.isEmpty()) {
            infor.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            totalButton.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
           infor.setVisibility(View.VISIBLE);
           recyclerView.setVisibility(View.VISIBLE);
           totalButton.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }


}