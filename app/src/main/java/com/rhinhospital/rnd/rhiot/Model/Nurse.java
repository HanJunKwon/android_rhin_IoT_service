package com.rhinhospital.rnd.rhiot.Model;

import com.google.gson.annotations.SerializedName;

public class Nurse {
    @SerializedName(value="emp_no")
    private String empNo;
    @SerializedName(value="department_nm")
    private String department_nm;
    @SerializedName(value="position_nm")
    private String position_nm;
    @SerializedName(value="nurse_nm")
    private String nurse_nm;
    @SerializedName(value="nurse_age")
    private int nurse_age;
    @SerializedName(value="nurse_gender")
    private char nurse_gender;

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getDepartment_nm() {
        return department_nm;
    }

    public void setDepartment_nm(String department_nm) {
        this.department_nm = department_nm;
    }

    public String getPosition_nm() {
        return position_nm;
    }

    public void setPosition_nm(String position_nm) {
        this.position_nm = position_nm;
    }

    public String getNurse_nm() {
        return nurse_nm;
    }

    public void setNurse_nm(String nurse_nm) {
        this.nurse_nm = nurse_nm;
    }

    public int getNurse_age() {
        return nurse_age;
    }

    public void setNurse_age(int nurse_age) {
        this.nurse_age = nurse_age;
    }

    public char getNurse_gender() {
        return nurse_gender;
    }

    public void setNurse_gender(char nurse_gender) {
        this.nurse_gender = nurse_gender;
    }

    Nurse(){

    }

}
