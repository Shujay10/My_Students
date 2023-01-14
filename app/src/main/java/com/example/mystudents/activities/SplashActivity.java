package com.example.mystudents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mystudents.MainActivity;
import com.example.mystudents.R;
import com.example.mystudents.struct.ClassStruct;
import com.example.mystudents.struct.StoreStruct;
import com.example.mystudents.struct.TimeTableStruct;
import com.example.mystudents.struct.SubjectsStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private static int splashTimer = 100;
    //private static int splashTimer = 10000;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mStore;

    FirebaseDatabase database;

    Intent toLogin;
    Intent toMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        toLogin = new Intent(SplashActivity.this, LogInActivity.class);
        toMain = new Intent(SplashActivity.this, MainActivity.class);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mStore = FirebaseFirestore.getInstance();

        splashHandler();
        //storeClasses();
//        storeSubject();
//        storeCla();

    }

    void storeSubject(){

        ArrayList<String> fin = new ArrayList<>();

        fin.add("Maths");
        fin.add("English");
        fin.add("Science");
        fin.add("Hindi");
        fin.add("Social");

        SubjectsStruct send = new SubjectsStruct(fin);

        mStore.collection("TUA")
                .document("Subjects").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        StoreStruct appData = new StoreStruct((ArrayList<String>) documentSnapshot.get("subject"), (ArrayList<String>) null);
                        System.out.println(appData);
                        Gson gson = new Gson();

                        String storeApp = gson.toJson(appData);

                        SharedPreferences prefs =  getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Subjects",storeApp);
                        editor.apply();
                    }
                });




    }

    void storeCla(){

        ArrayList<String> kin = new ArrayList<>();

        kin.add("LKG");
        kin.add("UKG");

        for(int i=1;i<13;i++){
            kin.add(String.valueOf(i));
        }

        ClassStruct send = new ClassStruct(kin);

        mStore.collection("TUA")
                .document("Class").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        StoreStruct appData = new StoreStruct((ArrayList<String>) null, (ArrayList<String>)  documentSnapshot.get("grade"));
                        System.out.println(appData);
                        Gson gson = new Gson();

                        String storeApp = gson.toJson(appData);

                        SharedPreferences prefs =  getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Classes",storeApp);
                        editor.apply();

                    }
                });

    }

    void storeClasses(){

        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] let = {"m","t","w","th","f","s"};

        int cls = 12;
        String txt;


        for(int x = 1;x<13;x++){
        cls = x;
        for(int i=0;i<6;i++){

            txt = let[i];

            ArrayList<String> list = new ArrayList<>();
            list.add("English_"+txt+cls);
            list.add("Science_"+txt+cls);
            list.add("Tamil_"+txt+cls);
            list.add("PT_"+txt+cls);
            list.add("Social_"+txt+cls);
            list.add("Maths_"+txt+cls);
            list.add("Hindi_"+txt+cls);
            list.add("Reading Club_"+txt+cls);

            TimeTableStruct stu = new TimeTableStruct(days[i],list);

            mStore.collection("TUA")
                    .document("Timetable").collection("Class "+cls)
                    .document(days[i]).set(stu);

            if(i==5){
                Toast.makeText(getApplicationContext(),"Stored "+cls,Toast.LENGTH_SHORT).show();
            }

        }
        }




    }

    private void splashHandler(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(toLogin);

                if(mUser == null)
                    startActivity(toLogin);
                else
                    startActivity(toMain);

            }
        },splashTimer);

    }

}