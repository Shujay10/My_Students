package com.example.mystudents.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.R;
import com.example.mystudents.struct.StaffStruct;

import java.util.ArrayList;

public class AddFacRvAdapter extends RecyclerView.Adapter<AddFacRvAdapter.ExampleHolder>{

    ArrayList<StaffStruct> list;

    public AddFacRvAdapter(ArrayList<StaffStruct> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_recycler,parent,false);

        return new AddFacRvAdapter.ExampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int position) {

        holder.facName.setText(list.get(position).getName());
        holder.facDesi.setText(list.get(position).getDesignation());
        holder.facSubject.setText(list.get(position).getSubject());
        holder.facID.setText(list.get(position).getStaffID());
        holder.facSchool.setText(list.get(position).getSchool());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExampleHolder extends RecyclerView.ViewHolder {

        TextView facName;
        TextView facDesi;
        TextView facSubject;
        TextView facID;
        TextView facSchool;


        public ExampleHolder(@NonNull View itemView) {
            super(itemView);

            facName = itemView.findViewById(R.id.facName);
            facDesi = itemView.findViewById(R.id.facDesi);
            facSubject = itemView.findViewById(R.id.facSubject);
            facID = itemView.findViewById(R.id.facID);
            facSchool = itemView.findViewById(R.id.facSchool);

        }
    }
}
