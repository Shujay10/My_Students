package com.example.mystudents.struct;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StoreStruct {

    String regNo;
    String school;

    ArrayList<String> subject;
    ArrayList<String> classes;

    @NonNull
    @Override
    public String toString() {

        String print1 = subject +"   "+classes;
        String print2 = regNo + "    " + school;
        String print3 = regNo + "    " + school + "     " + subject +"   "+classes;

        return print3;
    }

    public StoreStruct(String regNo, String school, ArrayList<String> subjects, ArrayList<String> classes) {
        this.regNo = regNo;
        this.school = school;
        this.subject = subjects;
        this.classes = classes;
    }

    public StoreStruct(String regNo, String school) {
        this.regNo = regNo;
        this.school = school;
    }

    public StoreStruct(ArrayList<String> subjects, ArrayList<String> classes) {
        this.subject = subjects;
        this.classes = classes;
    }



    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }
}
