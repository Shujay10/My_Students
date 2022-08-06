package com.example.mystudents.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.R;
import com.example.mystudents.struct.HomeStruct;

import java.util.ArrayList;

public class AddHomeRvAdapter extends RecyclerView.Adapter<AddHomeRvAdapter.ExampleHolder>{

    ArrayList<HomeStruct> list;

    public AddHomeRvAdapter(ArrayList<HomeStruct> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_recycler,parent,false);

        return new ExampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int position) {

        holder.subject.setText(list.get(position).getSubject());
        holder.work.setText(list.get(position).getWork());
        holder.grade.setText("Class "+list.get(position).getGrade());

        if(list.get(position).isTest()){
            holder.isTest.setText("Test");
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExampleHolder extends RecyclerView.ViewHolder {

        TextView subject;
        TextView work;
        TextView isTest;
        TextView grade;

        public ExampleHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            work = itemView.findViewById(R.id.subjectContent);
            isTest = itemView.findViewById(R.id.test);
            grade = itemView.findViewById(R.id.grade);

        }

    }
}
