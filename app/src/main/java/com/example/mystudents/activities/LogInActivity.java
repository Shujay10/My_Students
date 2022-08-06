package com.example.mystudents.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.mystudents.R;
import com.example.mystudents.adapters.TabPager;
import com.google.android.material.tabs.TabLayout;

public class LogInActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {


    TabLayout tabLayout;
    ViewPager viewPager;
    TabPager tabPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewer);

        tabLayout.addTab(tabLayout.newTab().setText("LogIn"));
        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabPager = new TabPager(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(tabPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}