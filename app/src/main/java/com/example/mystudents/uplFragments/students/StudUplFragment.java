package com.example.mystudents.uplFragments.students;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystudents.Faculty;
import com.example.mystudents.R;
import com.example.mystudents.activities.UplSwitchActivity;
import com.example.mystudents.databinding.FragmentStudUplBinding;
import com.example.mystudents.struct.StudStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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


public class StudUplFragment extends Fragment {

    private FragmentStudUplBinding binding;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Uri uri;

    Button upl;
    Intent intent;
    Button uploder;
    ProgressBar bar;
    TextView success;

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
        binding = FragmentStudUplBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        intent = new Intent(getActivity(), UplSwitchActivity.class);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uploder = root.findViewById(R.id.Stuuploder);
        upl = root.findViewById(R.id.Stuupl);
        success = root.findViewById(R.id.Stusucc);
        bar = root.findViewById(R.id.StuuplBar);
        bar.setVisibility(ProgressBar.GONE);

        checkPermission();

        uploder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent file = new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file,requestCode);

            }
        });

        upl.setOnClickListener(new View.OnClickListener() {
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
        String path = "/storage/emulated/0/Download/Students.xlsx";
        //String[] splPath = path.split(":");
        String[] spl = path.split("/");
        String filName = spl[spl.length-1];
        uploder.setText(filName);
        //path = splPath[1];

        this.path = path;
    }

    private void readExcel() throws InvalidFormatException, IOException {

        int rCount;
        int cCount;
        boolean status;
        String[] details;

        Row row;
        Cell cell;

        CellValue value;
        StudStruct upl;

        ArrayList<Integer> failed = new ArrayList();

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
        rCount = 10;
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

            upl = new StudStruct(details[0],details[4],details[5],details[1],details[2],details[6],details[7],details[8],details[3]);

            boolean stat = true;

            //TODO : uncomment to uplode nd register
            //stat = uploadStudents(upl);
            if(!stat){
                failed.add(i);
            }
            signUp(details[1],details[8],upl);

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
        success.setText("uploaded !!");

    }

    private boolean uploadStudents(StudStruct temp){

        mStore.collection(temp.getSchool()).document("Human resources").collection("StudentList").document(temp.getRollNo()).set(temp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });

        //TODO : problen in return statement
        return true;
    }

    private void signUp(String email,String dob,StudStruct upl){

        String[] tmpDob = dob.split("-");
        String password = tmpDob[0]+tmpDob[1]+tmpDob[2];

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(getContext(), "Authentication Success.",
                            //Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Authentication failed "+upl.getName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}