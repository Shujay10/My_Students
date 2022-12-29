package com.example.mystudents;

import androidx.annotation.NonNull;

public class Faculty {

    static String name;
    static String empid;
    static String school;
    static String designation;
    static String field;
    static String email;
    static String phoNoP;
    static String level;
    static String dob;
    static String ttClass;
    static int pass = -99;

    @NonNull
    @Override
    public String toString() {

        String cla = name+" "+empid+" "+school;

        return cla;
    }

    public static int getPass() {
        return pass;
    }

    public static void setPass(int pass) {
        Faculty.pass = pass;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Faculty.name = name;
    }

    public static String getEmpid() {
        return empid;
    }

    public static void setEmpid(String empid) {
        Faculty.empid = empid;
    }

    public static String getSchool() {
        return school;
    }

    public static void setSchool(String school) {
        Faculty.school = school;
    }

    public static String getDesignation() {
        return designation;
    }

    public static void setDesignation(String designation) {
        Faculty.designation = designation;
    }

    public static String getField() {
        return field;
    }

    public static void setField(String field) {
        Faculty.field = field;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Faculty.email = email;
    }

    public static String getPhoNoP() {
        return phoNoP;
    }

    public static void setPhoNoP(String phoNoP) {
        Faculty.phoNoP = phoNoP;
    }

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        Faculty.level = level;
    }

    public static String getDob() {
        return dob;
    }

    public static void setDob(String dob) {
        Faculty.dob = dob;
    }

    public static String getTtClass() {
        return ttClass;
    }

    public static void setTtClass(String ttClass) {
        Faculty.ttClass = ttClass;
    }
}
