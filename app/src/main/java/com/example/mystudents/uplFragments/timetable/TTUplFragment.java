package com.example.mystudents.uplFragments.timetable;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.activities.UplSwitchActivity;
import com.example.mystudents.databinding.FragmentTTUplBinding;
import com.example.mystudents.struct.TimeTableStruct;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TTUplFragment extends Fragment implements
        AdapterView.OnItemSelectedListener{

    private FragmentTTUplBinding binding;
    private FirebaseFirestore mStore;
    private Uri uri;

    TextView status;
    Spinner claSel;
    Intent intent;
    Button filSel;
    Button upload;
    ProgressBar bar;

    ArrayList<String> item;
    ArrayAdapter adapter;

    int x;
    int requestCode = 1;
    private String path = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Faculty.setPass(23);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTTUplBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        intent = new Intent(getActivity(), UplSwitchActivity.class);
        mStore = FirebaseFirestore.getInstance();
        item = new ArrayList<>();

        status = root.findViewById(R.id.ttStaus);
        claSel = root.findViewById(R.id.ttClassSel);
        filSel = root.findViewById(R.id.ttFilpath);
        upload = root.findViewById(R.id.ttUpload);
        bar = root.findViewById(R.id.ttProbar);
        bar.setVisibility(ProgressBar.GONE);

        claSel.setOnItemSelectedListener(this);

        setAdapter();
        checkPermission();

        filSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent file = new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file,requestCode);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //TODO : Problem with loading Screen

                    Toast.makeText(getContext(),"Uploading ....",Toast.LENGTH_LONG).show();
                    readExcel();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void setAdapter() {

        item.add("LKG");
        item.add("UKG");

        for (int i=1 ;i<13;i++)
            item.add(String.valueOf(i));

        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claSel.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(this.requestCode == requestCode && resultCode == Activity.RESULT_OK){

            if(data == null)
                return;

            uri = data.getData();
            System.out.println(uri.getPath());

            path();

        }

    }

    private void checkPermission(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int per = this.getContext().checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            per += this.getContext().checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if(per != 0){
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1001);
            }else{
                System.out.println("All permissions ok !!");
            }
        }

    }

    private void path(){

        //TODO : Fixed path

        //String path = uri.getPath();
        //System.out.println("Path : "+path);
        String path = "/storage/emulated/0/Download/Class 2.xlsx";
        //String[] splPath = path.split(":");
        String[] spl = path.split("/");
        String filName = spl[spl.length-1];
        filSel.setText(filName);
        //path = splPath[1];

        this.path = path;
    }

    private void readExcel() throws InvalidFormatException, IOException {

        int rCount;
        int cCount;
        //boolean statusB = false;
        String[] details;

        Row row;
        Cell cell;

        CellValue value;
        TimeTableStruct upl;

        ArrayList<Integer> failed = new ArrayList();
        ArrayList<String> cla = new ArrayList();

        bar.setVisibility(ProgressBar.VISIBLE);

        File file = new File(path);
        InputStream input = new FileInputStream(file);

        XSSFWorkbook book = new XSSFWorkbook(input);
        FormulaEvaluator formulaEvaluator = book.getCreationHelper().createFormulaEvaluator();
        XSSFSheet sheet = book.getSheetAt(0);

        rCount = sheet.getPhysicalNumberOfRows();
        row = sheet.getRow(0);
        cCount = row.getPhysicalNumberOfCells();

        System.out.println(rCount);
        System.out.println(cCount);

        //TODO : Fixed Rows

        //rCount = 59;
        rCount = 6;
        cCount = 9;

        details = new String[cCount];

        for(int i=1;i<=rCount;i++){

            row = sheet.getRow(i);
            cCount = row.getPhysicalNumberOfCells();
            cCount = 9;

            //TODO : Type casting

            for(int j=0;j<cCount;j++){
                cell = row.getCell(j);
                value = formulaEvaluator.evaluate(cell);

                details[j] = value.getStringValue();

            }

            for(int m=1;m<9;m++)
                cla.add(details[m]);

            upl = new TimeTableStruct(details[0],cla);

            boolean stat = true;

            //TODO : uncomment to uplode nd register
            stat = uploadTeachers(upl);
            if(!stat){
                failed.add(i);
            }

            cla.clear();
        }

        //TODO : End message

        if (failed.isEmpty()){
            Toast.makeText(getContext(),"All Success",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Failed "+failed.size(),Toast.LENGTH_SHORT).show();
            System.out.println(failed);
        }

        bar.setVisibility(ProgressBar.GONE);
        status.setText("uploaded !!");

    }

    private boolean uploadTeachers(TimeTableStruct temp){

        // TODO : return statement

        mStore.collection(Faculty.getSchool())
                .document("Timetable").collection("Class "+item.get(x))
                .document(temp.getDay()).set(temp);

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        x = i;
        //toast("Selected : "+x);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void toast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}