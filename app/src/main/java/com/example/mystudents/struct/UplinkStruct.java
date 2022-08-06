package com.example.mystudents.struct;

public class UplinkStruct {

    int logo;
    String title;

    public UplinkStruct() {
    }

    public UplinkStruct(int logo, String title) {
        this.logo = logo;
        this.title = title;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
