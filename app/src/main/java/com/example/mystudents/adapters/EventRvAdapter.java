package com.example.mystudents.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.R;
import com.example.mystudents.struct.EventStruct;

import java.util.ArrayList;

public class EventRvAdapter extends RecyclerView.Adapter<EventRvAdapter.ExampleHolder>{

    ArrayList<EventStruct> list;

    public EventRvAdapter(ArrayList<EventStruct> list) {
        this.list = list;
    }

    public class ExampleHolder extends RecyclerView.ViewHolder {

        TextView head;
        TextView content;
        TextView iss_Date;
        TextView end_Date;

        public ExampleHolder(@NonNull View itemView) {
            super(itemView);

            head = itemView.findViewById(R.id.ann_head);
            content = itemView.findViewById(R.id.ann_content);
            iss_Date = itemView.findViewById(R.id.iss_date);
            end_Date = itemView.findViewById(R.id.end_date);


        }
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_event,parent,false);

        return new ExampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int position) {

        holder.head.setText(list.get(position).getTitle());
        holder.content.setText(list.get(position).getContent());

        String[] issDt = list.get(position).getIssue().split(" ");
        String[] endDt = list.get(position).getEnd().split(" ");

        holder.iss_Date.setText(issDt[0]);
        holder.end_Date.setText(endDt[0]);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
