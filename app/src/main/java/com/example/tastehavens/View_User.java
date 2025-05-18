package com.example.tastehavens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class View_User extends AppCompatActivity {

    private TableLayout userTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_row);

        userTable = findViewById(R.id.userTable);
        ImageButton back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_User.this,Admin_Portal.class);
            }
        });

        addUserRow("1", new Helper("John Smith", "jsmith", "john@example.com", "customer", "pass123", 500, "Yes", "", 2));
        addUserRow("2", new Helper("Alice Brown", "abrown", "alice@example.com", "customer", "pass456", 750, "No", "", 3));
        addUserRow("3", new Helper("David Green", "dgreen", "david@example.com", "customer", "pass789", 300, "Yes", "", 1));
        addUserRow("1", new Helper("John Smith", "jsmith", "john@example.com", "customer", "pass123", 500, "Yes", "", 2));
        addUserRow("2", new Helper("Alice Brown", "abrown", "alice@example.com", "customer", "pass456", 750, "No", "", 3));
        addUserRow("3", new Helper("David Green", "dgreen", "david@example.com", "customer", "pass789", 300, "Yes", "", 1));
        addUserRow("1", new Helper("John Smith", "jsmith", "john@example.com", "customer", "pass123", 500, "Yes", "", 2));
        addUserRow("2", new Helper("Alice Brown", "abrown", "alice@example.com", "customer", "pass456", 750, "No", "", 3));
        addUserRow("3", new Helper("David Green", "dgreen", "david@example.com", "customer", "pass789", 300, "Yes", "", 1));
        addUserRow("1", new Helper("John Smith", "jsmith", "john@example.com", "customer", "pass123", 500, "Yes", "", 2));
        addUserRow("2", new Helper("Alice Brown", "abrown", "alice@example.com", "customer", "pass456", 750, "No", "", 3));
        addUserRow("3", new Helper("David Green", "dgreen", "david@example.com", "customer", "pass789", 300, "Yes", "", 1));
        addUserRow("1", new Helper("John Smith", "jsmith", "john@example.com", "customer", "pass123", 500, "Yes", "", 2));
        addUserRow("2", new Helper("Alice Brown", "abrown", "alice@example.com", "customer", "pass456", 750, "No", "", 3));
        addUserRow("3", new Helper("David Green", "dgreen", "david@example.com", "customer", "pass789", 300, "Yes", "", 1));
    }

    private void addUserRow(String uid, Helper user) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        TextView name = new TextView(this);
        name.setText(user.getNames());
        name.setPadding(8, 8, 8, 8);

        TextView email = new TextView(this);
        email.setText(user.getEmails());
        email.setPadding(8, 8, 8, 8);

        TextView amount = new TextView(this);
        amount.setText("R" + user.getAmount());
        amount.setPadding(8, 8, 8, 8);

        TextView orders = new TextView(this);
        orders.setText(String.valueOf(user.getActiveOrders()));
        orders.setPadding(8, 8, 8, 8);

        TextView reservation = new TextView(this);
        reservation.setText(user.getReservationStatus() != null ? user.getReservationStatus() : "No");
        reservation.setPadding(8, 8, 8, 8);

        // Delete button
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteButton.setBackgroundColor(Color.TRANSPARENT);
        deleteButton.setOnClickListener(v -> {
            userTable.removeView(row);
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
        });

        row.addView(name);
        row.addView(email);
        row.addView(amount);
        row.addView(orders);
        row.addView(reservation);
        row.addView(deleteButton);

        userTable.addView(row);
    }

    // Standalone Helper class
    public static class Helper {
        private String name;
        private String username;
        private String email;
        private String password;
        private String role;
        private int amount;
        private String reservationStatus;
        private int activeOrders;
        private String other;

        public Helper() {}

        public Helper(String name, String username, String email, String role, String password, int amount, String reservationStatus, String other, int activeOrders) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.role = role;
            this.password = password;
            this.amount = amount;
            this.reservationStatus = reservationStatus;
            this.other = other;
            this.activeOrders = activeOrders;
        }

        public String getNames() { return name; }
        public String getEmails() { return email; }
        public int getAmount() { return amount; }
        public String getReservationStatus() { return reservationStatus; }
        public int getActiveOrders() { return activeOrders; }
    }
}
