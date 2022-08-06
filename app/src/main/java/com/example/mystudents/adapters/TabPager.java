package com.example.mystudents.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mystudents.loginFragments.LogInFragment;
import com.example.mystudents.loginFragments.RegFragment;

public class TabPager extends FragmentStatePagerAdapter {

    int tabCount;

    public TabPager(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:{
                return new LogInFragment();
            }
            case 1:{
                return new RegFragment();
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
