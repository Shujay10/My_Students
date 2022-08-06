package com.example.mystudents.struct;

import androidx.annotation.NonNull;

public class HomeStruct {

    String grade;
    String subject;
    String work;
    boolean isTest;

    public HomeStruct() {
    }

    public HomeStruct(String grade, String subject, String work, boolean isTest) {
        this.grade = grade;
        this.subject = subject;
        this.work = work;
        this.isTest = isTest;
    }

    @NonNull
    @Override
    public String toString() {

        String msg = grade+" "+subject+" "+work+" "+isTest;

        return msg;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
