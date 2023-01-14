package com.example.mystudents.loginFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mystudents.MainActivity;
import com.example.mystudents.R;
import com.example.mystudents.databinding.FragmentLogInBinding;
import com.example.mystudents.struct.StoreStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.OutputStreamWriter;
import java.util.concurrent.Executor;


public class LogInFragment extends Fragment {

    private FragmentLogInBinding binding;

    EditText sclName,reNo,email,pass;
    ProgressBar proBar;

    boolean isGood = true;

    Button login;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        sclName = root.findViewById(R.id.SchoolName);
        reNo = root.findViewById(R.id.regNo);
        email = root.findViewById(R.id.emailId);
        pass = root.findViewById(R.id.passWord);
        login = root.findViewById(R.id.logIn);

        proBar = root.findViewById(R.id.progressBar);
        proBar.setVisibility(ProgressBar.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proBar.setVisibility(View.VISIBLE);
                validate();

            }
        });

        return root;
    }

    private boolean validate(){

        boolean isOk = true;

        if(getSchool().isEmpty()){
            sclName.setError("Required");
            isOk = false;
        }

        if(getRegNo().isEmpty()){
            reNo.setError("Required");
            isOk = false;
        }

        if(getEmail().isEmpty()){
            email.setError("Required");
            isOk = false;
        }

        if(getPass().isEmpty()){
            pass.setError("Required");
            isOk = false;
        }

        if(isOk){
            setValues();
        }

        if(!isGood){
            isOk = false;
        }

        // TODO create a class with static var for school,regno
        // and get details of student from database and store it in the class

        return isOk;

    }

    private void setValues() {

        DocumentReference docRef = mStore.collection(getSchool()).document("Human resources").collection("FacultyList").document(getRegNo());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String email1 = (String) documentSnapshot.get("email");

                if(documentSnapshot.exists()){
                    putValue(email1);

                }else {
                    boolean isGood = false;
                    proBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Data Not Found Firestore",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void putValue(String email1) {

        System.out.println(email1 +" == "+getEmail());

        if(!email1.equals(getEmail())){
            isGood = false;
            email.setError("Not a match");
        }

        if(isGood){

            StoreStruct userData = new StoreStruct(getRegNo(),getSchool());
            Gson gson = new Gson();

            String storeUser = gson.toJson(userData);
            SharedPreferences prefs = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserData",storeUser);
            editor.apply();
            auth();
        }else {
            proBar.setVisibility(View.GONE);
            Toast.makeText(getContext(),"Something wrong",Toast.LENGTH_SHORT).show();
            isGood = true;
        }

    }

    private void auth() {

        String email = getEmail();
        String password = getPass();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    proBar.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "Authentication successful.",
                            Toast.LENGTH_SHORT).show();
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }else {
                    proBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private String getSchool(){

        System.out.println(sclName.getText());
        return sclName.getText().toString();
    }

    private String getRegNo(){

        System.out.println(reNo.getText());
        return reNo.getText().toString();
    }

    private String getEmail(){

        System.out.println(email.getText());
        return email.getText().toString();
    }

    private String getPass(){

        System.out.println(pass.getText());
        return pass.getText().toString();
    }

}