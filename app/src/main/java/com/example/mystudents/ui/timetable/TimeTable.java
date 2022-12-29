package com.example.mystudents.ui.timetable;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.adapters.ClassesTabPager;
import com.example.mystudents.databinding.TimeTableFragmentBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TimeTable extends Fragment{

    private TimeTableFragmentBinding binding;

    TabLayout tabLayout;
    ViewPager viewPager;
    ClassesTabPager tabPager;

    Dialog grade;
    Spinner getGrade;
    Button setGrade;

    ArrayAdapter setClassAdapter;
    ArrayList<String> setGradeList;

    FloatingActionButton classChanger;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = TimeTableFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setGradeList = new ArrayList<>();

        tabLayout = root.findViewById(R.id.tab);
        viewPager = root.findViewById(R.id.pager);
        classChanger = root.findViewById(R.id.changer);

        grade = new Dialog(getActivity());
        grade.setContentView(R.layout.cgclass_dialog);
        getGrade = grade.findViewById(R.id.spinner);
        setGrade = grade.findViewById(R.id.gradeBtn);

        getGradeAdapter();

        classChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade.show();
            }

        });

        setGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String grd = getGrade.getSelectedItem().toString();
                Faculty.setTtClass(grd);
                grade.dismiss();
                changeIcon();
                tabLayout.removeAllTabs();
                init();
            }
        });

        init();

        return root;
    }

    private void changeIcon() {

        int res = R.drawable.ic_book;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            classChanger.setImageDrawable(getResources().getDrawable(res, getActivity().getTheme()));
        } else {
            classChanger.setImageDrawable(getResources().getDrawable(R.drawable.ic_book));
        }

    }

    private void getGradeAdapter() {

        for(int i=1;i<13;i++) {
            setGradeList.add(String.valueOf(i));
        }

        setClassAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,setGradeList);
        setClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getGrade.setAdapter(setClassAdapter);

    }

    private void init(){

        tabLayout.addTab(tabLayout.newTab().setText("Mon"));
        tabLayout.addTab(tabLayout.newTab().setText("Tue"));
        tabLayout.addTab(tabLayout.newTab().setText("Wed"));
        tabLayout.addTab(tabLayout.newTab().setText("Thu"));
        tabLayout.addTab(tabLayout.newTab().setText("Fri"));
        tabLayout.addTab(tabLayout.newTab().setText("Sat"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabPager = new ClassesTabPager(getChildFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(tabPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

    }

}