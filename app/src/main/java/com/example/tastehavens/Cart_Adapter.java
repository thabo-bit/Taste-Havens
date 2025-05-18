package com.example.tastehavens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartViewHolder> {

    private Context context;
    private List<Cart_items> cartItems;
    private OnItemClickListener listener;
    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onRemoveClick(int position);
        void onQuantityChanged(int position, int newQuantity);
    }

    public Cart_Adapter(Context context, List<Cart_items> cartItems, OnItemClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_looks, parent, false);
        return new CartViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart_items item = cartItems.get(position);

        holder.mealName.setText(item.getMealName());
        holder.price.setText(formatPrice(item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Load image with Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.home) // Add a placeholder
                .error(R.drawable.event) // Add an error image
                .into(holder.imageView);

        // Update Firestore when quantity changes via direct input
        holder.quantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int newQuantity = Integer.parseInt(holder.quantity.getText().toString());
                    if (newQuantity > 0 && newQuantity != item.getQuantity()) {
                        updateQuantityInFirestore(item, newQuantity, position);
                    }
                } catch (NumberFormatException e) {
                    holder.quantity.setText(String.valueOf(item.getQuantity()));
                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String formatPrice(String price) {
        // Format price string (e.g., ensure it has R prefix)
        if (!price.startsWith("R")) {
            return "R" + price;
        }
        return price;
    }

    private void updateQuantityInFirestore(Cart_items item, int newQuantity, int position) {
        if (item.getDocumentId() != null) {
            db.collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("cart")
                    .document(item.getDocumentId())
                    .update("quantity", newQuantity)
                    .addOnSuccessListener(aVoid -> {
                        item.setQuantity(newQuantity);
                        if (listener != null) {
                            listener.onQuantityChanged(position, newQuantity);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                    });
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<Cart_items> newItems) {
        cartItems.clear();
        cartItems.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mealName, price, quantity;
        View add, remove;

        public CartViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView9);
            mealName = itemView.findViewById(R.id.Meal);
            price = itemView.findViewById(R.id.Price);
            quantity = itemView.findViewById(R.id.quantity);
            add = itemView.findViewById(R.id.adding);
            remove = itemView.findViewById(R.id.substract);

            add.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAddClick(position);
                }
            });

            remove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRemoveClick(position);
                }
            });

            // Add long click listener for item removal
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    // You can add a remove item callback to your interface if needed
                    return true;
                }
                return false;
            });
        }
    }
}