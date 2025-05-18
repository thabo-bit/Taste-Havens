package com.example.tastehavens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<String> tabTitles;
    private List<String> tabData;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity,
                            List<String> tabTitles, List<String> tabData) {
        super(fragmentActivity);
        this.tabTitles = tabTitles;
        this.tabData = tabData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("tabTitle", tabTitles.get(position));
        args.putString("tabData", tabData.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return tabTitles.size();
    }
}
