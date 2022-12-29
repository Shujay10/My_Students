package com.example.mystudents.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.R;

import java.util.ArrayList;

public class ClassesRvAdapter extends RecyclerView.Adapter<ClassesRvAdapter.ExampleHolder>{

    ArrayList<String> list;

    public ClassesRvAdapter(ArrayList<String> list) {

        this.list = list;
    }

    public class ExampleHolder extends RecyclerView.ViewHolder {

        TextView period;

        public ExampleHolder(@NonNull View itemView) {
            super(itemView);

            period = itemView.findViewById(R.id.period_rv);

        }
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_recycler,parent,false);

        return new ExampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int position) {

        //System.out.println(list.get(position));

        holder.period.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        //System.out.println("Size : "+list.size());
        return list.size();

    }


}
