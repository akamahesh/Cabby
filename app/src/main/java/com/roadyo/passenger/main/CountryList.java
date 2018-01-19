package com.roadyo.passenger.main;

/**
 * Created by rahul on 2/6/16.
 */
public class CountryList
{
    String nameCode;
    String phoneCode;
    String name;

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryList(String nameCode, String phoneCode, String name) {
        this.nameCode = nameCode;
        this.phoneCode = phoneCode;
        this.name = name;
    }
}
