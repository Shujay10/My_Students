package com.example.mystudents.struct;

import java.util.ArrayList;

public class SubjectsStruct {

    ArrayList<String> subject;

    public SubjectsStruct(ArrayList<String> subject) {
        this.subject = subject;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }
}
