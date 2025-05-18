package com.example.tastehavens;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Arrays;
import java.util.List;

public class orders_history extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);







        List<String> tabTitles = Arrays.asList("Pending", "Preparing", "Ready", "History");
        List<String> tabData = Arrays.asList(
                "Pending orders: You have 3 pending orders.",
                "Preparing orders: Your order is being prepared.",
                "Ready orders: Your order is ready for pickup.",
                "History: View your order history here."
        );
        int[] tabIcons = {R.drawable.active, R.drawable.inactive_order, R.drawable.order_approve, R.drawable.inactive_order};

        // Pass the tab titles and data to the adapter
        viewPager.setAdapter(new ViewPagerAdapter(this, tabTitles, tabData));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = getLayoutInflater().inflate(R.layout.tab_item, null);

            ImageView tabIcon = customTab.findViewById(R.id.tab_icon);
            TextView tabText = customTab.findViewById(R.id.tab_text);

            tabIcon.setImageDrawable(ContextCompat.getDrawable(this, tabIcons[position]));
            tabText.setText(tabTitles.get(position));

            tab.setCustomView(customTab);
        }).attach();

    }
}
