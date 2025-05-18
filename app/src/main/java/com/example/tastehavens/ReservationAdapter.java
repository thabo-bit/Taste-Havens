package com.example.tastehavens;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<ReservationDetails> reservationList;
    private static final String TAG = "ReservationAdapter";

    public ReservationAdapter(Context context, List<ReservationDetails> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_reservation_looks, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationDetails details = reservationList.get(position);

        // Safely set text values with null checks
        setTextSafely(holder.date, details.getReserveDate());
        setTextSafely(holder.time, details.getReserveTime());
        setTextSafely(holder.guests, details.getReserveGuests());
        setTextSafely(holder.name, details.getReserveCusName());
        setTextSafely(holder.phone, details.getReserveNumber());

        holder.accept.setOnClickListener(v -> {
            if (details.getDocumentId() != null) {
                showBottomSheet(v.getContext(), details);
            } else {
                Log.e(TAG, "Document ID is null for position: " + position);
                Toast.makeText(context, "Error: Missing reservation ID", Toast.LENGTH_SHORT).show();
            }
        });

        holder.reject.setOnClickListener(v -> {
            if (details.getDocumentId() != null) {
                showBottomSheetReject(v.getContext(), details);
            } else {
                Log.e(TAG, "Document ID is null for position: " + position);
                Toast.makeText(context, "Error: Missing reservation ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    // Helper method to safely set text with null checks
    private void setTextSafely(TextView textView, String text) {
        if (textView != null) {
            textView.setText(text != null ? text : "N/A");
        }
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, guests, name, phone;
        Button accept, reject;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.reservation_date);
            time = itemView.findViewById(R.id.reservation_time);
            guests = itemView.findViewById(R.id.reservation_guests);
            name = itemView.findViewById(R.id.reservation_name);
            phone = itemView.findViewById(R.id.reservation_phone);
            accept = itemView.findViewById(R.id.reservation_accept);
            reject = itemView.findViewById(R.id.reservation_reject);
        }
    }

    private void showBottomSheet(Context context, ReservationDetails details) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.reason_bottom_looks, null);

        TextView reasonText = bottomSheetView.findViewById(R.id.reasonTextView);
        TextView tableInput = bottomSheetView.findViewById(R.id.reasonEditText);
        Button btnAssign = bottomSheetView.findViewById(R.id.rejectButton);

        reasonText.setText("Please Assign Them A Table Number");
        tableInput.setHint("Assign Table Number");
        tableInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnAssign.setText("Assign");

        btnAssign.setOnClickListener(v -> {
            String tableNumber = tableInput.getText().toString().trim();

            if (tableNumber.isEmpty()) {
                Toast.makeText(context, "Please enter a table number", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Reservations")
                    .document(details.getDocumentId())
                    .update(
                            "tableNumber", tableNumber,
                            "acceptanceStatus", "Confirmed"
                    )
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Reservation Confirmed", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        // Optional: Refresh data
                        if (context instanceof Accept_Reservation) {
                            ((Accept_Reservation) context).fetchData();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to confirm reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Confirm reservation error", e);
                    });
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void showBottomSheetReject(Context context, ReservationDetails details) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.reason_bottom_looks, null);

        TextView reasonText = bottomSheetView.findViewById(R.id.reasonTextView);
        TextView reasonInput = bottomSheetView.findViewById(R.id.reasonEditText);
        Button btnReject = bottomSheetView.findViewById(R.id.rejectButton);

        reasonText.setText("Please provide a reason for rejection");
        reasonInput.setHint("Reason for rejection");
        btnReject.setText("Reject");

        btnReject.setOnClickListener(v -> {
            String reason = reasonInput.getText().toString().trim();

            if (reason.isEmpty()) {
                Toast.makeText(context, "Please provide a reason", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Reservations")
                    .document(details.getDocumentId())
                    .update(
                            "acceptanceStatus", "Rejected",
                            "reason", reason
                    )
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Reservation Rejected", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        // Optional: Refresh data
                        if (context instanceof Accept_Reservation) {
                            ((Accept_Reservation) context).fetchData();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to reject reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Reject reservation error", e);
                    });
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    // Method to update data
    public void updateData(List<ReservationDetails> newList) {
        reservationList = newList;
        notifyDataSetChanged();
    }
}