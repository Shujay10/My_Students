package com.example.mystudents.ui.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.adapters.EventRvAdapter;
import com.example.mystudents.databinding.EventFragmentBinding;
import com.example.mystudents.struct.EventStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.grpc.Server;

public class Event extends Fragment {

    private EventFragmentBinding binding;
    private FirebaseDatabase mData;
    private Calendar myCalendar;

    Dialog addEventDia;
    EditText titleDia;
    EditText dateDia;
    EditText contentDia;
    Button addEvDia;

    FloatingActionButton addEvent;
    RecyclerView viewEvent;

    ArrayList<EventStruct> list;
    EventRvAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = EventFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myCalendar = Calendar.getInstance();
        mData = FirebaseDatabase.getInstance();
        addEventDia = new Dialog(getActivity());
        list = new ArrayList<>();

        addEvent = root.findViewById(R.id.addEvent);
        viewEvent = root.findViewById(R.id.eventRV);

        addEventDia.setContentView(R.layout.addent_dialog);
        titleDia = addEventDia.findViewById(R.id.eventTitle);
        dateDia = addEventDia.findViewById(R.id.eventDate);
        contentDia = addEventDia.findViewById(R.id.eventContent);
        addEvDia = addEventDia.findViewById(R.id.addEve);

        setAdapter();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Faculty.getLevel().equals("4") || Faculty.getLevel().equals("3")){
                    addEventDia.show();
                }else {
                    toast("Access Denied");
                }

            }
        });

        addEvDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Valid()){
                    addData();
                    tvClear();
                    addEventDia.dismiss();
                }
            }
        });

        dateDia.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });

        return root;
    }

    private void addData() {

        EventStruct upload;

        String title = titleDia.getText().toString();
        String date = dateDia.getText().toString();
        String content = contentDia.getText().toString();

        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat(" HH:mm:ss");
        String issue = df.format(d1);
        String time = df1.format(df1);

        date = date+time;

        upload = new EventStruct(title,content,issue,date);
        list.add(upload);
        adapter.notifyDataSetChanged();

        mData.getReference().child(Faculty.getSchool()).child("Events")
                .child(title).setValue(upload)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toast("Added");
                        }else {
                            toast("Problem");
                        }
                    }
                });

    }

    private boolean Valid() {

        boolean isVal = true;

        String title = titleDia.getText().toString();
        String date = dateDia.getText().toString();
        String content = contentDia.getText().toString();

        if( title.isEmpty()) {
            isVal = false;
            titleDia.setError("Required");
        }

        if( date.isEmpty()) {
            isVal = false;
            dateDia.setError("Required");
        }

        if( content.isEmpty()) {
            isVal = false;
            contentDia.setError("Required");
        }

        return isVal;
    }

    private void setAdapter() {

        adapter = new EventRvAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewEvent.setItemAnimator(new DefaultItemAnimator());
        viewEvent.setLayoutManager(layoutManager);
        viewEvent.setAdapter(adapter);
    }

    private void tvClear() {

        titleDia.setText("");
        dateDia.setText("");
        contentDia.setText("");
    }

    private void updateLabel(){

        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateDia.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void toast(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();
    }

}