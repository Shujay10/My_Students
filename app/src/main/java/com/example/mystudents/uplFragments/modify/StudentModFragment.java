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
import com.example.mystudents.databinding.FragmentStudentModBinding;
import com.example.mystudents.struct.StaffStruct;
import com.example.mystudents.struct.StudStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentModFragment extends Fragment {

    private FragmentStudentModBinding binding;
    private FirebaseFirestore mStore;

    Intent intent;

    Dialog getId;
    EditText inpId;
    Button search;

    EditText name;
    EditText parentsName;
    EditText stuId;
    EditText email;
    EditText dob;
    EditText phoPri;
    EditText phoSec;
    EditText school;
    Spinner grade;
    Button submit;

    ArrayList<String> gradeList;
    ArrayAdapter gradeAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Faculty.setPass(23);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStudentModBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        intent = new Intent(getActivity(), UplSwitchActivity.class);
        mStore = FirebaseFirestore.getInstance();
        getId = new Dialog(getActivity());
        gradeList = new ArrayList<>();

        name = root.findViewById(R.id.stuEdtName);
        parentsName = root.findViewById(R.id.stuEdtPName);
        stuId = root.findViewById(R.id.stuEdtID);
        email = root.findViewById(R.id.stuEdtEmail);
        dob = root.findViewById(R.id.stuEdtdob);
        phoPri = root.findViewById(R.id.stuEdtPhno);
        phoSec = root.findViewById(R.id.stuEdtPhno1);
        school = root.findViewById(R.id.stuEdtSchool);
        grade = root.findViewById(R.id.stuEdtGrade);
        submit = root.findViewById(R.id.stuEdtSubmit);

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

        StudStruct upload;

        String name = this.name.getText().toString();
        String parentsName = this.parentsName.getText().toString();
        String email = this.email.getText().toString();
        String stuId = this.stuId.getText().toString();
        String dob = this.dob.getText().toString();
        String phoPri = this.phoPri.getText().toString();
        String phoSec = this.phoSec.getText().toString();
        String school = this.school.getText().toString();
        String grade = this.grade.getSelectedItem().toString();

        upload = new StudStruct(name,grade,stuId,email,parentsName,phoPri,phoSec,dob,school);

        mStore.collection(Faculty.getSchool()).document("Human resources")
                .collection("StudentList").document(stuId).set(upload)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
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
        String parentsName = this.parentsName.getText().toString();
        String email = this.email.getText().toString();

        String dob = this.dob.getText().toString();
        String phoPri = this.phoPri.getText().toString();
        String phoSec = this.phoSec.getText().toString();
        String school = this.school.getText().toString();

        if(name.isEmpty() || parentsName.isEmpty() || email.isEmpty()){
            isVal = false;
        }

        if(dob.isEmpty() || phoPri.isEmpty() || phoSec.isEmpty() || school.isEmpty()){
            isVal = false;
        }

        return isVal;
    }

    private void available(String id) {

        mStore.collection(Faculty.getSchool()).document("Human resources")
                .collection("StudentList").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                getId.dismiss();
                                StudStruct exchange;
                                exchange = document.toObject(StudStruct.class);
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

    private void getDetails(StudStruct cloud) {

        name.setText(cloud.getName());
        parentsName.setText(cloud.getParentName());
        stuId.setText(cloud.getRollNo());
        // TODO : stuId.setEnabled(false);
        email.setText(cloud.getEmail());
        dob.setText(cloud.getDateOfBirth());
        phoPri.setText(cloud.getPhPri());
        phoSec.setText(cloud.getPhSec());
        school.setText(cloud.getSchool());

        int index = gradeList.indexOf(cloud.getGrade());
        grade.setSelection(index);

    }

    private void setAdapter() {

        addData();

        gradeAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,gradeList);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(gradeAdapter);

    }

    private void addData() {

        gradeList.add("LKG");
        gradeList.add("UKG");

        for(int i=1;i<13;i++)
            gradeList.add(String.valueOf(i));
    }

    private void toast(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();
    }
    
}