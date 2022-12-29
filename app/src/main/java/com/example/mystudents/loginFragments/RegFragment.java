package com.example.mystudents.loginFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.R;
import com.example.mystudents.activities.RegisterActivity;
import com.example.mystudents.databinding.FragmentRegBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks {

    private FragmentRegBinding binding;

    Button submit;
    EditText passIn;
    TextView contact;

    private String passWord;

    FirebaseFirestore mStore;

    GoogleApiClient googleApiClient;
    CheckBox checkBox;
    private String siteKey = "6Ld3cLgjAAAAACSmKqhbYXEN4jt_EHLXGGSV1bxw";
    private boolean isVerified = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        submit = root.findViewById(R.id.subPass);
        passIn = root.findViewById(R.id.regPass);
        contact = root.findViewById(R.id.gmail);
        checkBox = root.findViewById(R.id.checkBox);

        mStore = FirebaseFirestore.getInstance();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(RegFragment.this)
                .build();
        googleApiClient.connect();
        findPass();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,siteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status stat = recaptchaTokenResult.getStatus();

                                    if((stat != null) && stat.isSuccess()){
                                        toast("Verified");
                                        isVerified = true;
                                    }
                                }
                            });
                } else{
                    toast("Not Verified");
                    isVerified = false;
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gmil = contact.getText().toString();
                //System.out.println(gmil);
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

                // TODO : check the condition
                if(pass.equals(passWord) && isVerified){
                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    startActivity(intent);
                }else if (!pass.equals(passWord) && isVerified){
                    toast("Incorrect Password");
                }else if(pass.equals(passWord) && !isVerified){
                    toast("Please Verify");
                }else {
                    toast("Error");
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    void toast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}