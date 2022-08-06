package com.example.mystudents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.activities.LogInActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystudents.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    TextView viewName;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        setSupportActionBar(binding.appBarMain.toolbar);

        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile,R.id.nav_home,R.id.nav_event,
                R.id.nav_timeTable, R.id.nav_gallery,R.id.nav_upl,
                R.id.nav_settings,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        viewName = findViewById(R.id.txt);
        viewName.setText(Faculty.name);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Toast.makeText(MainActivity.this,"User Not Found",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        }else {
            try {
                getVal();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this,"User Found",Toast.LENGTH_SHORT).show();
        }

    }

    private void getVal() throws FileNotFoundException {

        try {
            InputStream in = openFileInput("FacStore.txt");
            if (in != null) {
                InputStreamReader tmp=new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String str;
                StringBuilder buf=new StringBuilder();

                while ((str = reader.readLine()) != null) {
                    buf.append(str);
                }
                in.close();
                String get = buf.toString();
                String[] splitting = get.split(":");
                Faculty.empid = splitting[0];
                Faculty.school = splitting[1];

            }
        }
        catch (java.io.FileNotFoundException e) {
        }
        catch (Throwable t) {

            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();

        }

        String sch = Faculty.school;

        DocumentReference docRef = mStore.collection(sch).document("Human resources")
                .collection("FacultyList").document(Faculty.empid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    Faculty.name = (String) documentSnapshot.get("name");

                }else {
                    boolean isGood = false;
                    Toast.makeText(getApplicationContext(),"Data Not Found Firestore",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}