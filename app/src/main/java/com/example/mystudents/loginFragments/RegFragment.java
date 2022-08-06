package com.example.mystudents.loginFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.R;
import com.example.mystudents.activities.RegisterActivity;
import com.example.mystudents.databinding.FragmentRegBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegFragment extends Fragment {

    private FragmentRegBinding binding;

    Button submit;
    EditText passIn;
    TextView contact;

    private String passWord;

    FirebaseFirestore mStore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        submit = root.findViewById(R.id.subPass);
        passIn = root.findViewById(R.id.regPass);
        contact = root.findViewById(R.id.gmail);

        mStore = FirebaseFirestore.getInstance();

        findPass();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gmil = contact.getText().toString();
                System.out.println(gmil);
                setClipboard(getContext(),gmil);
            }
        });

        return root;
    }

    private void check(String pass){

        passWord = pass;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = passIn.getText().toString();
                System.out.println(pass);

                System.out.println(passWord+" from db");

                // TODO : change the condition
                if(pass.equals(passWord)){
                    //if(true){
                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    startActivity(intent);
                }else {
                    System.out.println("Error");
                }

            }
        });

    }

    @SuppressLint("ObsoleteSdkInt")
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPass(){

        DocumentReference docRef = mStore.collection("Admin").document("PassWord");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String word = documentSnapshot.get("RegPass").toString();
                check(word);
            }
        });

    }

}