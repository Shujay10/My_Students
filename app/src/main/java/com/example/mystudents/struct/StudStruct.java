package com.example.mystudents.struct;

public class StudStruct {

    String name;
    String grade;
    String rollNo;
    String email;
    String parentName;
    String phPri;
    String phSec;
    String DateOfBirth;
    String school;

    public StudStruct() {
    }

    public StudStruct(String name, String grade, String rollNo, String email, String parentName, String phPri, String phSec, String dateOfBirth, String school) {
        this.name = name;
        this.grade = grade;
        this.rollNo = rollNo;
        this.email = email;
        this.parentName = parentName;
        this.phPri = phPri;
        this.phSec = phSec;
        DateOfBirth = dateOfBirth;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhPri() {
        return phPri;
    }

    public void setPhPri(String phPri) {
        this.phPri = phPri;
    }

    public String getPhSec() {
        return phSec;
    }

    public void setPhSec(String phSec) {
        this.phSec = phSec;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
