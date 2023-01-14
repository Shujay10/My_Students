package com.example.mystudents.struct;

import java.util.ArrayList;

public class ClassStruct {

    ArrayList <String> grade;

    public ClassStruct(ArrayList<String> grade) {
        this.grade = grade;
    }

    public ArrayList<String> getGrade() {
        return grade;
    }

    public void setGrade(ArrayList<String> grade) {
        this.grade = grade;
    }
}
