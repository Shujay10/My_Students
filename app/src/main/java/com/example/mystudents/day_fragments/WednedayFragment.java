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
import com.example.mystudents.struct.ClassStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class WednedayFragment extends Fragment {

    RecyclerView wed;
    ArrayList<String> list;

    FirebaseFirestore mStore;

    ClassesRvAdapter adapter;

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){

        View view = inflater.inflate(R.layout.wedneday_fragment,container,false);

         wed= view.findViewById(R.id.recycler_wednesday);

        mStore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        fetch_Data();

        return view;
    }

    private void fetch_Data(){

        mStore.collection("Shemford").document("Timetable")
                .collection("Class "+Faculty.getTtClass()/* TODO : Class */).document("Wednesday").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ClassStruct temp = documentSnapshot.toObject(ClassStruct.class);
                        if (temp != null) {
                            list = temp.getPeriods();
                        }

                        adapter = new ClassesRvAdapter(list);
                        RecyclerView.LayoutManager layoutManager;
                        layoutManager = new LinearLayoutManager(getActivity());
                        wed.setLayoutManager(layoutManager);
                        wed.setAdapter(adapter);
                    }
                });

    }

}