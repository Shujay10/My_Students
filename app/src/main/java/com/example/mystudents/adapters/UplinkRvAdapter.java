package com.example.mystudents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudents.R;
import com.example.mystudents.activities.UplinkInf;
import com.example.mystudents.struct.UplinkStruct;

import java.util.ArrayList;


public class UplinkRvAdapter extends RecyclerView.Adapter<UplinkRvAdapter.ExampleHolder> {

    ArrayList<UplinkStruct> list;
    Context context;
    UplinkInf inf;

    public UplinkRvAdapter(ArrayList<UplinkStruct> list, Context context, UplinkInf inf) {
        this.list = list;
        this.context = context;
        this.inf = inf;
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uplink_recycler,parent,false);

        return new UplinkRvAdapter.ExampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.logo.setImageResource(list.get(position).getLogo());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExampleHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView logo;

        public ExampleHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_upl);
            logo = itemView.findViewById(R.id.logo_upl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inf != null){
                        int po = getAdapterPosition();

                        if(po != RecyclerView.NO_POSITION){
                            inf.onClickItem(po);
                        }

                    }
                }
            });
        }
    }
}
