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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    TextView greet;
    RecyclerView recyclerView;
    List<MenuItems> menuItemsList = new ArrayList<>();
    List<MenuItems> fullMenuList = new ArrayList<>();

    SearchView searchView;
    ImageButton searchButton;
    boolean isSearchVisible = false;
    HorizontalScrollView horizontalScrollView;
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
                    startActivity(new Intent(Menu.this, Profile.class));
                    finish();
                    return true;
                } else if (id == R.id.orders) {
                    startActivity(new Intent(Menu.this, orders_history.class));
                    finish();
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(Menu.this, Cart.class));
                    finish();
                    return true;
                } else if (id == R.id.reservation) {
                    startActivity(new Intent(Menu.this, Incoming_orders.class));
                    finish();
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

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new Items_Adapter(menuItemsList);
        recyclerView.setAdapter(adapter);
        loadMenuItems();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadMenuItems();
            swipeRefreshLayout.setRefreshing(false);
        });

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

        findViewById(R.id.burger).setOnClickListener(v -> filterMenuByCategory("Burger"));
        findViewById(R.id.pizza).setOnClickListener(v -> filterMenuByCategory("Pizza"));
        findViewById(R.id.drinks).setOnClickListener(v -> filterMenuByCategory("Drinks"));
        findViewById(R.id.disert).setOnClickListener(v -> filterMenuByCategory("Dessert"));
        findViewById(R.id.chickens).setOnClickListener(v -> filterMenuByCategory("Chicken"));
        findViewById(R.id.salad).setOnClickListener(v -> filterMenuByCategory("Salad"));
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
                        String description = document.getString("description");
                        String category = document.getString("category");

                        menuItemsList.add(new MenuItems(name, price, imageUrl, description, category));
                    }
                    fullMenuList = new ArrayList<>(menuItemsList);
                    adapter = new Items_Adapter(menuItemsList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Menu.this, "Failed to load menu items", Toast.LENGTH_SHORT).show();
                });
    }

    private void filterMenuItems(String query) {
        List<MenuItems> filteredList = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (MenuItems item : fullMenuList) {
            if (item.getMenuName().toLowerCase().contains(lowerQuery) ||
                    item.getDiscription().toLowerCase().contains(lowerQuery) ||
                    item.getCategory().toLowerCase().contains(lowerQuery)) {
                filteredList.add(item);
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
        int cx = searchButton.getRight() - (searchButton.getWidth() / 2);
        int cy = searchButton.getTop() + (searchButton.getHeight() / 2);
        float finalRadius = (float) Math.hypot(searchView.getWidth(), searchView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(searchView, cx, cy, 0, finalRadius);
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
                if (imm != null) {
                    imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        anim.start();
    }

    private void hideSearchView() {
        int cx = searchButton.getRight() - (searchButton.getWidth() / 2);
        int cy = searchButton.getTop() + (searchButton.getHeight() / 2);
        float initialRadius = (float) Math.hypot(searchView.getWidth(), searchView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(searchView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchView.setVisibility(View.GONE);
                isSearchVisible = false;
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                searchView.setQuery("", false);
                horizontalScrollView.setVisibility(View.VISIBLE);
                horizontalScrollView.animate().alpha(1f).setDuration(200).start();
            }
        });
        anim.start();
    }
}
