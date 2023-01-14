package com.example.mystudents.day_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.adapters.ClassesRvAdapter;
import com.example.mystudents.struct.TimeTableStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class SaturdayFragment extends Fragment {

    RecyclerView sat;
    ArrayList<String> list;

    FirebaseFirestore mStore;

    ClassesRvAdapter adapter;

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){

        View view = inflater.inflate(R.layout.saturday_fragment,container,false);

         sat= view.findViewById(R.id.recycler_saturday);

        mStore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        fetch_Data();

        return view;
    }

    private void fetch_Data(){

        mStore.collection(Faculty.getSchool()).document("Timetable")
                .collection("Class "+Faculty.getTtClass()/* TODO : Class */).document("Saturday").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        TimeTableStruct temp = documentSnapshot.toObject(TimeTableStruct.class);
                        if (temp != null) {
                            list = temp.getPeriods();
                        }

                        adapter = new ClassesRvAdapter(list);
                        RecyclerView.LayoutManager layoutManager;
                        layoutManager = new LinearLayoutManager(getActivity());
                        sat.setLayoutManager(layoutManager);
                        sat.setAdapter(adapter);
                    }
                });

    }

}