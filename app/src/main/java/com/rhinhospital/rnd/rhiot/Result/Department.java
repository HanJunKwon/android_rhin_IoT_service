package com.rhinhospital.rnd.rhiot.Result;

import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName(value="department_cd")
    private String department_cd; // 부서 코드번호

    @SerializedName(value="department_nm")
    private String department_nm; // 부서명
}
