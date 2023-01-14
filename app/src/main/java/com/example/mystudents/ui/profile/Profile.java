package com.example.mystudents.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.databinding.ProfileFragmentBinding;
import com.example.mystudents.struct.StoreStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Profile extends Fragment {

    private ProfileFragmentBinding binding;

    TextView name,jbLevel,empid,email;
    TextView desi,field,phone,dob,school;

    Gson gson;

    ProgressBar progressBar;

    FirebaseFirestore mStore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mStore = FirebaseFirestore.getInstance();
        gson = new Gson();

        name = root.findViewById(R.id.proName);
        jbLevel = root.findViewById(R.id.proJBL);
        empid = root.findViewById(R.id.proRoll);
        email = root.findViewById(R.id.proEmail);
        desi = root.findViewById(R.id.proDesi);
        field = root.findViewById(R.id.proField);
        phone = root.findViewById(R.id.proPhone);
        dob = root.findViewById(R.id.proDob);
        school = root.findViewById(R.id.proSchool);
        progressBar = root.findViewById(R.id.proPro_bar);

        try {
            getVal();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }

    private void getVal() throws FileNotFoundException {

        SharedPreferences prefs =  getContext().getSharedPreferences("UserData", MODE_PRIVATE);

        String usrDat = prefs.getString("UserData","");
        StoreStruct tr1 = gson.fromJson(usrDat, StoreStruct.class);
        System.out.println(tr1);
        Faculty.setEmpid(tr1.getRegNo());
        Faculty.setSchool(tr1.getSchool());

        String sch = Faculty.getSchool();

        DocumentReference docRef = mStore.collection(sch).document("Human resources")
                .collection("FacultyList").document(Faculty.getEmpid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    Faculty.setName((String) documentSnapshot.get("name"));
                    Faculty.setEmail((String) documentSnapshot.get("email"));
                    Faculty.setPhoNoP((String) documentSnapshot.get("phoneNo"));
                    Faculty.setDesignation((String) documentSnapshot.get("designation"));
                    Faculty.setLevel((String) documentSnapshot.get("jobLevel"));
                    Faculty.setField((String) documentSnapshot.get("subject"));
                    Faculty.setDob((String) documentSnapshot.get("dateOfBirth"));
                    Faculty.setSchool(sch);
                    System.out.println(Faculty.class);
                    setVal();

                }else {
                    boolean isGood = false;
                    Toast.makeText(getContext(),"Data Not Found Firestore",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setVal() {

        progressBar.setVisibility(View.GONE);
        name.setText(Faculty.getName());

        jbLevel.setText("JL : "+Faculty.getLevel());

        empid.setText(Faculty.getEmpid());
        email.setText(Faculty.getEmail());
        desi.setText(Faculty.getDesignation());
        field.setText(Faculty.getField());
        phone.setText(Faculty.getPhoNoP());
        dob.setText(Faculty.getDob());
        school.setText(Faculty.getSchool());

    }

}