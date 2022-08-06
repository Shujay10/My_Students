package com.example.mystudents.uplFragments.modify;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.activities.UplSwitchActivity;
import com.example.mystudents.databinding.FragmentFacultyModBinding;
import com.example.mystudents.struct.StaffStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FacultyModFragment extends Fragment {

    private FragmentFacultyModBinding binding;
    private FirebaseFirestore mStore;

    Intent intent;

    Dialog getId;
    EditText inpId;
    Button search;

    EditText name;
    EditText subject;
    EditText facId;
    EditText email;
    EditText dob;
    EditText phoneNo;
    EditText school;
    Spinner desi;
    Spinner jobLvl;
    Button submit;

    ArrayList<String> desiList;
    ArrayAdapter desiAdapter;
    ArrayList<String> jbLvlList;
    ArrayAdapter jbLvlAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Faculty.setPass(23);
        startActivity(intent);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFacultyModBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        intent = new Intent(getActivity(), UplSwitchActivity.class);
        mStore = FirebaseFirestore.getInstance();
        getId = new Dialog(getActivity());
        desiList = new ArrayList<>();
        jbLvlList = new ArrayList<>();

        name = root.findViewById(R.id.facEdtName);
        subject = root.findViewById(R.id.facEdtSub);
        facId = root.findViewById(R.id.facEdtID);
        email = root.findViewById(R.id.facEdtEmail);
        dob = root.findViewById(R.id.facEdtdob);
        phoneNo = root.findViewById(R.id.facEdtPhno);
        school = root.findViewById(R.id.facEdtSchool);
        desi = root.findViewById(R.id.facEdtDesi);
        jobLvl = root.findViewById(R.id.facEdtJobLvl);
        submit = root.findViewById(R.id.facEdtSubmit);

        getId.setContentView(R.layout.getid_dialog);
        inpId = getId.findViewById(R.id.findId);
        search = getId.findViewById(R.id.findBtn);

        setAdapter();
        getId.show();
        //TODO : getId.setCancelable(false);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = inpId.getText().toString();
                if(id.isEmpty()){
                    inpId.setError("Required");
                }else {
                    available(id);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid()){
                    addVal();
                }else {
                    toast("Fill All Fields");
                }
            }
        });

        return root;
    }

    private void addVal() {

        StaffStruct upload;

        String name = this.name.getText().toString();
        String subject = this.subject.getText().toString();
        String email = this.email.getText().toString();
        String facId = this.facId.getText().toString();
        String dob = this.dob.getText().toString();
        String phoneNo = this.phoneNo.getText().toString();
        String school = this.school.getText().toString();
        String desi = this.desi.getSelectedItem().toString();
        String jLvl = this.jobLvl.getSelectedItem().toString();

        upload = new StaffStruct(name,desi,subject,facId,school,email,phoneNo,jLvl,dob);

        mStore.collection(Faculty.getSchool()).document("Human resources")
                .collection("FacultyList").document(facId).set(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toast("Edit Success");
                        }else {
                            toast("Edit Failed");
                        }
                    }
                });

    }

    private boolean valid() {

        boolean isVal = true;

        String name = this.name.getText().toString();
        String subject = this.subject.getText().toString();
        String email = this.email.getText().toString();

        String dob = this.dob.getText().toString();
        String phoneNo = this.phoneNo.getText().toString();
        String school = this.school.getText().toString();

        if(name.isEmpty() || subject.isEmpty() || email.isEmpty()){
            isVal = false;
        }

        if(dob.isEmpty() || phoneNo.isEmpty() || school.isEmpty()){
            isVal = false;
        }

        return isVal;
    }

    private void available(String id) {

        mStore.collection(Faculty.getSchool()).document("Human resources")
                .collection("FacultyList").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                getId.dismiss();
                                StaffStruct exchange;
                                exchange = document.toObject(StaffStruct.class);
                                getDetails(exchange);
                            } else {
                                toast("ID not Found");
                            }
                        } else {
                            toast("Unable to access the cloud");
                        }
                    }
                });

    }

    private void getDetails(StaffStruct cloud){

        name.setText(cloud.getName());
        subject.setText(cloud.getSubject());
        facId.setText(cloud.getStaffID());
        // TODO : facId.setEnabled(false);
        email.setText(cloud.getEmail());
        dob.setText(cloud.getDateOfBirth());
        phoneNo.setText(cloud.getPhoneNo());
        school.setText(cloud.getSchool());

        int jIndex = Integer.parseInt(cloud.getJobLevel()) - 1;
        jobLvl.setSelection(jIndex);

        switch (cloud.getDesignation()){
            case "IT Staff":{
                desi.setSelection(0);
                break;
            }
            case "Principal":{
                desi.setSelection(1);
                break;
            }
            case "Vice Principal":{
                desi.setSelection(2);
                break;
            }
            case "Official":{
                desi.setSelection(3);
                break;
            }
            case "Teacher":{
                desi.setSelection(4);
                break;
            }
        }

    }

    private void setAdapter(){

        addData();

        desiAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,desiList);
        desiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desi.setAdapter(desiAdapter);

        jbLvlAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,jbLvlList);
        jbLvlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobLvl.setAdapter(jbLvlAdapter);

    }

    private void addData(){

        desiList.add("IT Staff");
        desiList.add("Principal");
        desiList.add("Vice Principal");
        desiList.add("Official");
        desiList.add("Teacher");

        jbLvlList.add("1");
        jbLvlList.add("2");
        jbLvlList.add("3");
        jbLvlList.add("4");
    }

    private void toast(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();
    }

}