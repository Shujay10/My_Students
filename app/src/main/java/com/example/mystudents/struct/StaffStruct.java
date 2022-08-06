package com.example.mystudents.struct;

import androidx.annotation.NonNull;

public class StaffStruct {

    String name;
    String designation;
    String subject;
    String staffID;
    String school;
    String email;
    String phoneNo;
    String jobLevel;
    String dateOfBirth;

    public StaffStruct() {

    }

    public StaffStruct(String name, String designation, String subject, String staffID, String school, String email, String phoneNo, String jobLevel, String dateOfBirth) {
        this.name = name;
        this.designation = designation;
        this.subject = subject;
        this.staffID = staffID;
        this.school = school;
        this.email = email;
        this.phoneNo = phoneNo;
        this.jobLevel = jobLevel;
        this.dateOfBirth = dateOfBirth;
    }

    @NonNull
    @Override
    public String toString() {

        String print = name+" "+designation+" "+subject+" "+staffID+" "+school+" "+email+" "+phoneNo+" "+jobLevel+" "+dateOfBirth;

        return print;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String  jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
