package com.example.mystudents.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mystudents.R;
import com.example.mystudents.adapters.AddFacRvAdapter;
import com.example.mystudents.struct.StaffStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    FloatingActionButton fab;
    boolean havIT = false;
    Dialog schoolReg;
    Dialog caution;
    Dialog facReg;
    String sch;
    Button comp;
    int x;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    Button done;

    EditText school;
    EditText confirm;
    Button schoolSubmit;

    EditText name;
    Spinner desi;
    EditText subject;
    EditText empId;
    EditText email;
    EditText pass;
    EditText cpass;
    Button facSubmit;

    RecyclerView facList;
    AddFacRvAdapter adapter;
    ArrayAdapter arrayAdapter;
    ArrayList<StaffStruct> list;

    ArrayList<String > desiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inzSchoolReg();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        //inzFactDetails();
        fab = findViewById(R.id.floatingActionButton);
        facList = findViewById(R.id.addFac);
        comp = findViewById(R.id.regCom);
        desiList = new ArrayList<>();
        list = new ArrayList<>();
        setAdapter();
        addv();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inzFactDetails();
                facReg.show();

            }
        });

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);

                if(havIT)
                    startActivity(intent);
                else
                    caution();

            }
        });

    }

    private void caution() {

        caution = new Dialog(RegisterActivity.this);
        caution.setContentView(R.layout.caution_dialog);
        done = caution.findViewById(R.id.done);

        caution.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caution.dismiss();
            }
        });

    }

    private void inzFactDetails() {

        facReg = new Dialog(RegisterActivity.this);
        facReg.setContentView(R.layout.getfac_dialog);

        name = facReg.findViewById(R.id.facInpName);
        desi = facReg.findViewById(R.id.facInpDesi);
        subject = facReg.findViewById(R.id.facInpSub);
        empId = facReg.findViewById(R.id.facInpID);

        email = facReg.findViewById(R.id.facInpEmail);
        pass = facReg.findViewById(R.id.facInpPass);
        cpass = facReg.findViewById(R.id.facInpRePass);

        facSubmit = facReg.findViewById(R.id.facInpSubmit);

        name.setHint("Name");
        empId.setHint("Emp ID");
        email.setHint("Email ID");
        pass.setHint("Password");
        cpass.setHint("Confirm");

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,desiList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desi.setAdapter(arrayAdapter);

        desi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                x = i;
                if(getDesi(i).equals("IT Staff")){
                    subject.setHint("Field");
                }else {
                    subject.setHint("Subject");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        facSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valid()){
                    addVal();
                }else {
                    Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean valid() {

        boolean isVal = true;

        String nam = name.getText().toString();
        String sub = subject.getText().toString();
        String id = empId.getText().toString();
        String eml = email.getText().toString();
        String passwd = pass.getText().toString();
        String cpassd = cpass.getText().toString();

        if(nam.isEmpty() || sub.isEmpty() || id.isEmpty()){
            isVal = false;
        }

        if(eml.isEmpty() || passwd.isEmpty() || cpassd.isEmpty() ){
            isVal = false;
        }

        if(passwd.length() < 6){
            isVal = false;
            pass.setError("Too Short");
        }

        if(!passwd.equals(cpassd)){
            isVal = false;
            cpass.setError("Not A Match");
            cpass.setText("");
        }


        return isVal;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addVal() {

        String nam = name.getText().toString();
        String desi = desiList.get(x);
        String sub = subject.getText().toString();
        String id = empId.getText().toString();
        String jl ;

        String eml = email.getText().toString();
        String passwd = pass.getText().toString();

        if(desi.equals("Principal")){
            desiList.remove("Principal");
            System.out.println(desiList);
            arrayAdapter.notifyDataSetChanged();
        }

        if(desi.equals("Vice Principal")){
            desiList.remove("Vice Principal");
            System.out.println(desiList);
            arrayAdapter.notifyDataSetChanged();
        }

        if(desi.equals("IT Staff")){
            jl = "4";
        }else if(desi.equals("Principal")){
            jl = "3";
        }else if(desi.equals("Vice Principal")){
            jl = "3";
        }else {
            jl = "0";
        }

        StaffStruct temp = new StaffStruct(nam,desi,sub,id,sch,eml,"N/A",jl,"N/A");

        list.add(temp);
        adapter.notifyDataSetChanged();
        facReg.dismiss();

        if(temp.getDesignation().equals("IT Staff"))
            havIT = true;

        regUser(eml,passwd);
        putIn_db(temp);

    }

    private void regUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void inzSchoolReg(){

        schoolReg = new Dialog(RegisterActivity.this);
        schoolReg.setCanceledOnTouchOutside(false);
        schoolReg.setCancelable(false);
        schoolReg.setContentView(R.layout.getschool_dialog);
        school = schoolReg.findViewById(R.id.regSchool);
        confirm = schoolReg.findViewById(R.id.regSchool1);
        schoolSubmit = schoolReg.findViewById(R.id.regSchSub);

        schoolSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String snam = school.getText().toString();
                String cnam = confirm.getText().toString();

                System.out.println(snam +" "+cnam);

                if(!snam.isEmpty() && !cnam.isEmpty()) {
                    if (snam.equals(cnam)) {
                        sch = snam;
                        schoolReg.dismiss();
                        caution();
                    } else {
                        confirm.setError("Not Matching");
                        confirm.setText(null);
                    }
                }else {
                    school.setError("Required");
                    confirm.setError("Required");
                }


            }
        });

        schoolReg.show();

    }

    private void putIn_db(StaffStruct temp) {

        //mStore.collection(sch)
        mStore.collection(sch).document("Human resources").collection("FacultyList").document(temp.getStaffID()).set(temp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void addv(){

        desiList.add("IT Staff");
        desiList.add("Principal");
        desiList.add("Vice Principal");

    }

    private void setAdapter() {

        adapter = new AddFacRvAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        facList.setItemAnimator(new DefaultItemAnimator());
        facList.setLayoutManager(layoutManager);
        facList.setAdapter(adapter);

    }

    private String getDesi(int x){

        return desiList.get(x);
    }

}