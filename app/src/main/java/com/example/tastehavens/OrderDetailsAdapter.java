package com.example.tastehavens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderViewHolder> {

    private Context context;
    private List<orderDetails> orderList;

    public OrderDetailsAdapter(Context context, List<orderDetails> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_items, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        orderDetails order = orderList.get(position);

        holder.orderNumber.setText("#ORD-" + order.getOrderId());
        holder.orderDate.setText(order.getOrderTime());
        holder.customerName.setText("Customer Name: " + order.getCustomerName());
        holder.tableNumber.setText(order.getTableNumber());
        holder.orderStatus.setText("Preparing"); // Replace with dynamic status if you have it
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, orderDate, customerName, tableNumber, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderDate = itemView.findViewById(R.id.orderDate);
            customerName = itemView.findViewById(R.id.customerName);
            tableNumber = itemView.findViewById(R.id.tableNumber);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
}
