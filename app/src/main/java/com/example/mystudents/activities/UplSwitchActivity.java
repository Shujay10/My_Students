package com.example.mystudents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mystudents.Faculty;
import com.example.mystudents.MainActivity;
import com.example.mystudents.R;
import com.example.mystudents.uplFragments.modify.FacultyModFragment;
import com.example.mystudents.uplFragments.modify.StudentModFragment;
import com.example.mystudents.uplFragments.students.StudUplFragment;
import com.example.mystudents.uplFragments.teachers.TeachUplFragment;
import com.example.mystudents.uplFragments.timetable.TTUplFragment;

public class UplSwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_upl);


        switch (Faculty.getPass()){

            case 0:{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,new TeachUplFragment()).addToBackStack(null).commit();
                break;
            }
            case 1:{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,new StudUplFragment()).addToBackStack(null).commit();
                break;
            }
            case 2:{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,new TTUplFragment()).addToBackStack(null).commit();
                break;
            }
            case 4:{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,new FacultyModFragment()).addToBackStack(null).commit();
                break;
            }
            case 5:{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,new StudentModFragment()).addToBackStack(null).commit();
                break;
            }
            case 23:{
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
            }

        }



    }
}