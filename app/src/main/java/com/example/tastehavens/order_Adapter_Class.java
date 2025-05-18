package com.example.tastehavens;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class order_Adapter_Class extends RecyclerView.Adapter<order_Adapter_Class.OrderViewHolder> {

    private Context context;
    private List<orderDetails> orderList;
    private final String channelId = "order_notifications";
    private final int notificationId = 1001;

    public order_Adapter_Class(Context context, List<orderDetails> orderList) {
        this.context = context;
        this.orderList = orderList;
//        createNotificationChannel();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incoming_orders_looks, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        orderDetails order = orderList.get(position);

        holder.orderIdTextView.setText("Order #" + order.getOrderId());
        holder.tableNumberTextView.setText("Table: " + order.getTableNumber());
        holder.orderTimeTextView.setText("Time: " + order.getOrderTime());
        holder.customerName.setText("Customer: " + order.getCustomerName());

        holder.orders_cardview.setOnClickListener(v -> showBottomSheet(order, position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, tableNumberTextView, orderTimeTextView, customerName, SpeacialNotes;
        CardView orders_cardview;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id_text_view);
            tableNumberTextView = itemView.findViewById(R.id.table_number_text_view);
            orderTimeTextView = itemView.findViewById(R.id.order_time_text_view);
            customerName = itemView.findViewById(R.id.CustomerName);
            orders_cardview = itemView.findViewById(R.id.orders_cardview);
            SpeacialNotes = itemView.findViewById(R.id.specialNotes);
        }
    }

    private void showBottomSheet(orderDetails order, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.incoming_orders, null);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        TextView orderNumber = sheetView.findViewById(R.id.orderNumber);
        TextView customerName = sheetView.findViewById(R.id.customerName);
        TextView itemsList = sheetView.findViewById(R.id.order_items);
        TextView Notes = sheetView.findViewById(R.id.specialNotes);
        TextView Time = sheetView.findViewById(R.id.timeReceived);
        TextView Table = sheetView.findViewById(R.id.tableDelivery);
        Button accept = sheetView.findViewById(R.id.btnAccept);
        Button reject = sheetView.findViewById(R.id.btnReject);
        Button ready = sheetView.findViewById(R.id.btnReady);
        Button preparing = sheetView.findViewById(R.id.btnPreparing);
        Button almostReady = sheetView.findViewById(R.id.btnAlmostReady);
        LinearLayout show = sheetView.findViewById(R.id.preparationButtonsLayout);

        orderNumber.setText("Order #: " + order.getOrderId());
        customerName.setText("Customer: " + order.getCustomerName());
        Notes.setText("Special notes: " + order.getNotes());
        Time.setText("Time Received: " + order.getOrderTime());
        Table.setText("Order Type: " + order.getTableNumber());

        String orderId = order.getOrderId();

        StringBuilder items = new StringBuilder();
        for (Cart_items item : order.getCartItems()) {
            items.append("- ").append(item.getQuantity()).append("x ").append(item.getMealName()).append("\n");
        }
        itemsList.setText(items.toString().trim());

        bottomSheetDialog.setCancelable(false);

        reject.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Confirm Reject")
                    .setMessage("Are you sure you want to reject this order?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(context, "Order rejected", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        orderList.remove(order);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        accept.setOnClickListener(v -> {
            show.setVisibility(View.VISIBLE);
            accept.setVisibility(View.GONE);
            reject.setText("Dismiss Order");

            almostReady.setEnabled(false);
            almostReady.setBackgroundColor(Color.GRAY);

            ready.setEnabled(false);
            ready.setBackgroundColor(Color.GRAY);

            database.collection("AllOrders")
                    .document(orderId)
                    .update("status", "accepted");
        });

        preparing.setOnClickListener(v -> {
            almostReady.setEnabled(true);
            almostReady.setBackgroundColor(Color.rgb(30, 136, 229));
            preparing.setBackgroundColor(Color.GRAY);
            preparing.setEnabled(false);

            database.collection("AllOrders")
                    .document(orderId)
                    .update("status", "Preparing");
        });

        almostReady.setOnClickListener(v -> {
            ready.setEnabled(true);
            ready.setBackgroundColor(Color.rgb(56, 142, 60));
            almostReady.setEnabled(false);
            almostReady.setBackgroundColor(Color.GRAY);

            database.collection("AllOrders")
                    .document(orderId)
                    .update("status", "Almost Ready");
        });

        ready.setOnClickListener(v -> {
            database.collection("AllOrders")
                    .document(orderId)
                    .update("status", "ready")
                    .addOnSuccessListener(aVoid -> {


                        bottomSheetDialog.dismiss();
                        orderList.remove(order);
                        notifyItemRemoved(position);


                        database.collection("analytics")
                                .document("Stats")
                                .update("OrdersCompleted", FieldValue.increment(1));


                        String userId = auth.getCurrentUser().getUid();
                        database.collection("Users")
                                .document(userId)
                                .update("salary", FieldValue.increment(67));

                        database.collection("analytics")
                                .document("Stats")
                                .update("salaries",FieldValue.increment(67));




                    });
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

}