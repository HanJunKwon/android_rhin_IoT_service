package com.rhinhospital.rnd.rhiot.Result;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName(value="is_exists")
    private String is_exists;

    public String getIs_exists() {
        return is_exists;
    }

    public void setIs_exists(String is_exists) {
        this.is_exists = is_exists;
    }
}
