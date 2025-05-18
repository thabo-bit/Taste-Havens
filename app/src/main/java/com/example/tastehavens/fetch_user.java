package com.example.tastehavens;

public class fetch_user {
    public class Helper {
        private String name;
        private String username;
        private String email;
        private String password;
        private String role;
        private int amount;
        private String reservationStatus; // "Yes" or "No"
        private int activeOrders;
        private String other;

        public Helper() {
        }

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

        public String getNames() {
            return name;
        }

        public void setNames(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmails() {
            return email;
        }

        public void setEmails(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getReservationStatus() {
            return reservationStatus;
        }

        public void setReservationStatus(String reservationStatus) {
            this.reservationStatus = reservationStatus;
        }

        public int getActiveOrders() {
            return activeOrders;
        }

        public void setActiveOrders(int activeOrders) {
            this.activeOrders = activeOrders;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }
}