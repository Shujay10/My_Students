package com.example.mystudents.ui.homework;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.adapters.AddHomeRvAdapter;
import com.example.mystudents.databinding.FragmentHomeworkBinding;
import com.example.mystudents.struct.HomeStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HomeWorkFragment extends Fragment {

    private FragmentHomeworkBinding binding;
    private FirebaseDatabase mData;

    Dialog homeData;
    Spinner grade;
    Spinner subject;
    CheckBox isTest;
    EditText work;
    Button add;

    FloatingActionButton addHome;
    RecyclerView viweHome;
    Spinner viewClass;
    Button filter;

    ArrayAdapter viewClassAdapter;
    ArrayList<String> classList;

    ArrayAdapter setClassAdapter;
    ArrayList<String> setGradeList;
    ArrayAdapter setSubAdapter;
    ArrayList<String> setSubList;

    AddHomeRvAdapter adapter;
    ArrayList<HomeStruct> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeworkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mData = FirebaseDatabase.getInstance();
        homeData = new Dialog(getActivity());
        list = new ArrayList<>();
        classList = new ArrayList<>();
        setGradeList = new ArrayList<>();
        setSubList = new ArrayList<>();

        addHome = root.findViewById(R.id.homeAdd);
        viweHome = root.findViewById(R.id.homeWorkRV);
        viewClass = root.findViewById(R.id.classFil);
        filter = root.findViewById(R.id.filter);

        homeData.setContentView(R.layout.addhome_dialog);
        grade = homeData.findViewById(R.id.selClass);
        subject = homeData.findViewById(R.id.selSubject);
        isTest = homeData.findViewById(R.id.isTest);
        work = homeData.findViewById(R.id.work);
        add = homeData.findViewById(R.id.addHome);
        setAdapter();

        addHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeData.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valid()){
                    addWork();
                }

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clas = viewClass.getSelectedItem().toString();
                setView(clas);

            }
        });

        return root;
    }

    private void setView(String clas) {

        switch (clas){

            case "None":{
                list.clear();
                adapter.notifyDataSetChanged();
                break;
            }
            case "All":{

                for(int i=-1;i<13;i++){
                    String val = new String();
                    if(i==-1){
                        val = "UKG";
                    }else if(i == 0){
                        val = "LKG";
                    }else {
                        val = String.valueOf(i);
                    }

                    list.clear();
                    adapter.notifyDataSetChanged();

                    mData.getReference(Faculty.getSchool()).child("Homework").child("Class_"+val).get()
                            .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                                        HomeStruct add = ds.getValue(HomeStruct.class);
                                        list.add(add);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            });
                }

                if(list.isEmpty()){
                    toast("No HomeWork");
                }
                break;
            }
            default:{

                list.clear();
                adapter.notifyDataSetChanged();

                mData.getReference(Faculty.getSchool()).child("Homework").child("Class_"+clas).get()
                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){

                                    HomeStruct add = ds.getValue(HomeStruct.class);
                                    list.add(add);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        });

                if(list.isEmpty()){
                    toast("No HomeWork");
                }
                break;
            }

        }

    }

    private void addWork() {

        HomeStruct upload;
        String grade = this.grade.getSelectedItem().toString();
        String subject = this.subject.getSelectedItem().toString();
        String work = this.work.getText().toString();
        boolean isTest = this.isTest.isChecked();

        upload = new HomeStruct(grade,subject,work,isTest);

        System.out.println(upload);
        list.add(upload);
        adapter.notifyDataSetChanged();

//        mData.getReference().child(Faculty.getSchool()).child("Homework")
//                .child("Class_"+grade).child(subject).setValue(upload)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if(task.isSuccessful()){
//                    clear();
//                    toast("Added");
//                    homeData.dismiss();
//                }else {
//                    toast("Problem");
//                }
//            }
//        });

    }

    private void clear() {
        this.isTest.setSelected(false);
        this.work.setText("");
    }

    private boolean valid() {

        boolean isVal = true;

        String work = this.work.getText().toString();

        if(work.isEmpty()){
            isVal = false;
        }

        return  isVal;
    }

    private void setAdapter() {

        addData();

        adapter = new AddHomeRvAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viweHome.setItemAnimator(new DefaultItemAnimator());
        viweHome.setLayoutManager(layoutManager);
        viweHome.setAdapter(adapter);

        viewClassAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,classList);
        viewClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewClass.setAdapter(viewClassAdapter);

        setClassAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,setGradeList);
        setClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(setClassAdapter);

        setSubAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,setSubList);
        setSubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(setSubAdapter);
        
        
    }

    private void addData() {

        classList.add("None");
        classList.add("All");
        setGradeList.add("LKG");
        classList.add("LKG");
        setGradeList.add("UKG");
        classList.add("UKG");

        for(int i=1;i<13;i++) {
            setGradeList.add(String.valueOf(i));
            classList.add(String.valueOf(i));
        }

        setSubList.add("Maths");
        setSubList.add("English");
        setSubList.add("Science");
        setSubList.add("Hindi");
        setSubList.add("Social");
        setSubList.add("Computer");
        // TODO : do a custome dialog
        setSubList.add("Custom");
    }

    private void toast(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}