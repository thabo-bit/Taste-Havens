package com.example.tastehavens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Waiter_Menu extends AppCompatActivity {

    TextView greet;
    RecyclerView recyclerView;

    List<MenuItems> menuItemsList = new ArrayList<>();
    SearchView searchView;

    ImageButton searchButton;
    boolean isSearchVisible = false;
    HorizontalScrollView horizontalScrollView;

    List<MenuItems> fullMenuList = new ArrayList<>();
    Items_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        greet = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        searchButton = findViewById(R.id.searchButton);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profile) {
                    startActivity(new Intent(Waiter_Menu.this, Profile.class));
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(Waiter_Menu.this, orders_history.class));
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(Waiter_Menu.this, Cart.class));
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(Waiter_Menu.this, Incoming_orders.class));
                    return true;
                }

                return false;
            }
        });



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null) {
            greet.setText("Hello " + user.getDisplayName());
        } else {
            greet.setText("Hello");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(Waiter_Menu.this, 2));
        adapter = new Items_Adapter(menuItemsList);
        recyclerView.setAdapter(adapter);

        loadMenuItems();

        searchButton.setOnClickListener(v -> {

            ConstraintLayout.LayoutParams Params = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
            Params.setMargins(Params.leftMargin,140,Params.rightMargin,Params.bottomMargin);

            if (!isSearchVisible) {
                showSearchView();
            } else {
                hideSearchView();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });


        ImageButton burgerButton = findViewById(R.id.burger);
        burgerButton.setOnClickListener(v -> filterMenuByCategory("Burger"));

        ImageButton pizzaButton = findViewById(R.id.pizza);
        pizzaButton.setOnClickListener(v -> filterMenuByCategory("Pizza"));

        ImageButton drinksButton = findViewById(R.id.drinks);
        drinksButton.setOnClickListener(v -> filterMenuByCategory("Drinks"));

        ImageButton dessertButton = findViewById(R.id.disert);
        dessertButton.setOnClickListener(v -> filterMenuByCategory("Dessert"));

        ImageButton chickenButton = findViewById(R.id.chickens);
        chickenButton.setOnClickListener(v -> filterMenuByCategory("Chicken"));

        ImageButton saladButton = findViewById(R.id.salad);
        saladButton.setOnClickListener(v -> filterMenuByCategory("Salad"));
    }

    public void orders(View view) {
        Intent intent = new Intent(Waiter_Menu.this, chef_order.class);
        startActivity(intent);
    }

    private void loadMenuItems() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Menu")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    menuItemsList.clear();
                    for (var document : queryDocumentSnapshots.getDocuments()) {
                        String name = document.getString("name");
                        String price = "R" + document.getDouble("price");
                        String imageUrl = document.getString("imageUrl");
                        String Discription = document.getString("description");
                        String category = document.getString("category");

                        menuItemsList.add(new MenuItems(name, price, imageUrl, Discription, category));
                    }
                    fullMenuList = new ArrayList<>(menuItemsList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Waiter_Menu.this, "Failed to load menu items", Toast.LENGTH_SHORT).show();
                });
    }

    private void filterMenuItems(String query) {
        List<MenuItems> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(fullMenuList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (MenuItems item : fullMenuList) {
                if (item.getMenuName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getDiscription().toLowerCase().contains(lowerCaseQuery) ||
                        item.getCategory().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(item);
                }
            }
        }
        adapter = new Items_Adapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void filterMenuByCategory(String category) {
        List<MenuItems> filteredList = new ArrayList<>();
        for (MenuItems item : fullMenuList) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(item);
            }
        }
        adapter = new Items_Adapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void showSearchView() {
        int cx = searchButton.getWidth() / 2;
        int cy = searchButton.getHeight() / 2;
        float finalRadius = (float) Math.hypot(searchView.getWidth(), searchView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(searchView,
                searchButton.getRight() - (searchButton.getWidth() / 2),
                searchButton.getTop() + (searchButton.getHeight() / 2),
                0, finalRadius);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                searchView.setVisibility(View.VISIBLE);

                horizontalScrollView.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                    horizontalScrollView.setVisibility(View.GONE);
                    horizontalScrollView.setAlpha(1f);
                }).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSearchVisible = true;
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        anim.start();
    }

    private void hideSearchView() {
        // Circular hide animation
        int cx = searchButton.getWidth() / 2;
        int cy = searchButton.getHeight() / 2;
        float initialRadius = (float) Math.hypot(searchView.getWidth(), searchView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(searchView,
                searchButton.getRight() - (searchButton.getWidth() / 2),
                searchButton.getTop() + (searchButton.getHeight() / 2),
                initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

                horizontalScrollView.setVisibility(View.VISIBLE);
                horizontalScrollView.animate().alpha(1f).setDuration(200).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchView.setVisibility(View.GONE);
                isSearchVisible = false;


                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


                searchView.setQuery("", false);
            }
        });
        anim.start();
    }
}