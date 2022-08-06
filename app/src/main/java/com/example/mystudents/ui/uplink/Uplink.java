package com.example.mystudents.ui.uplink;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.activities.UplinkInf;
import com.example.mystudents.adapters.UplinkRvAdapter;
import com.example.mystudents.struct.UplinkStruct;
import com.example.mystudents.databinding.FragmentUplinkBinding;
import com.example.mystudents.activities.UplSwitchActivity;

import java.util.ArrayList;

public class Uplink extends Fragment implements UplinkInf {

    private FragmentUplinkBinding binding;

    RecyclerView uplinkRecycler;

    ArrayList<UplinkStruct> list;

    UplinkRvAdapter adapter;

    Dialog select;
    Button student;
    Button teacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUplinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = new ArrayList<>();
        uplinkRecycler = root.findViewById(R.id.up_rec);

        select = new Dialog(getActivity());
        select.setContentView(R.layout.stutea_dialog);
        student = select.findViewById(R.id.student);
        teacher = select.findViewById(R.id.teacher);


        setAdapter();
        addGrid();

        return root;

    }

    @SuppressLint("NotifyDataSetChanged")
    private void addGrid(){

        list.add(new UplinkStruct(R.drawable.ic_upl_sheet,"Teachers"));
        list.add(new UplinkStruct(R.drawable.ic_upl_sheet,"Students"));
        list.add(new UplinkStruct(R.drawable.ic_upl_tt,"Time Table"));
        list.add(new UplinkStruct(R.drawable.ic_upl_photos,"Photos"));
        list.add(new UplinkStruct(R.drawable.ic_modify,"Modify Data"));
        adapter.notifyDataSetChanged();
    }

    private void setAdapter(){

        adapter = new UplinkRvAdapter(list,getActivity(),this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        uplinkRecycler.setLayoutManager(layoutManager);
        uplinkRecycler.setAdapter(adapter);

    }

    @Override
    public void onClickItem(int pos) {


        Intent intent = new Intent(getActivity(), UplSwitchActivity.class);

        int level = Integer.parseInt(Faculty.getLevel());

        if(level > 3 && pos!=4){
            Faculty.setPass(pos);
            startActivity(intent);
        }else if(level > 3 && pos ==4 ){
            select.show();

            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Faculty.setPass(pos);
                    toast(String.valueOf(Faculty.getPass()));
                    select.dismiss();
                    startActivity(intent);
                }
            });

            student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Faculty.setPass(pos+1);
                    toast(String.valueOf(Faculty.getPass()));
                    select.dismiss();
                    startActivity(intent);

                }
            });
        }else {
            toast("Access Denied");
        }

    }

    private void toast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }

}