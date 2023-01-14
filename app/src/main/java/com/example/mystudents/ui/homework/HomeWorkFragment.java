package com.example.mystudents.ui.homework;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.mystudents.struct.StoreStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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

    ArrayAdapter viewClassAdapter;
    ArrayList<String> classList;

    ArrayAdapter setClassAdapter;
    ArrayList<String> setGradeList;
    ArrayAdapter setSubAdapter;
    ArrayList<String> setSubList;

    AddHomeRvAdapter adapter;
    ArrayList<HomeStruct> list;

    Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeworkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gson = new Gson();

        mData = FirebaseDatabase.getInstance();
        homeData = new Dialog(getActivity());
        list = new ArrayList<>();
        classList = new ArrayList<>();
        setGradeList = new ArrayList<>();
        setSubList = new ArrayList<>();

        addHome = root.findViewById(R.id.homeAdd);
        viweHome = root.findViewById(R.id.homeWorkRV);
        viewClass = root.findViewById(R.id.classFil);

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

        viewClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setView(classList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setView(String clas) {

        switch (clas){

            case "None":{
                list.clear();
                adapter.notifyDataSetChanged();
                break;
            }
            case "All":{

                list.clear();

                for(int i=0;i<setGradeList.size();i++){
                    String val ;
                    val = setGradeList.get(i);
                    adapter.notifyDataSetChanged();

                    int finalI = i;
                    mData.getReference(Faculty.getSchool()).child("Homework").child("Class_"+val).get()
                            .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                                        HomeStruct add = ds.getValue(HomeStruct.class);
                                        list.add(add);

                                    }

                                    if (finalI == setGradeList.size()-1 ){

                                        if(list.isEmpty())
                                            toast("No HW");

                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            });
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

                                if (list.isEmpty()){
                                    toast("No HW");
                                }

                            }
                        });

                break;
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
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

        mData.getReference().child(Faculty.getSchool()).child("Homework")
                .child("Class_"+grade).child(subject).setValue(upload)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    clear();
                    toast("Added");
                    homeData.dismiss();
                }else {
                    toast("Problem");
                }
            }
        });

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

        SharedPreferences prefs =  getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        String subDat = prefs.getString("Subjects","");
        String claDat = prefs.getString("Classes","");

        StoreStruct tr1 = gson.fromJson(subDat, StoreStruct.class);
        StoreStruct tr2 = gson.fromJson(claDat, StoreStruct.class);

        System.out.println(tr1);
        System.out.println(tr2);

        setGradeList.addAll(tr2.getClasses());
        classList.addAll(tr2.getClasses());
        setSubList.addAll(tr1.getSubject());

    }

    private void toast(String txt){

            Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();
    }

    private void toastL(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}