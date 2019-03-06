package com.rhinhospital.rnd.rhiot.Result;

import com.google.gson.annotations.SerializedName;

public class MeasurementHistory {
    @SerializedName(value="maximal_blood_pressure")
    private String maximalBloodPressure;
    @SerializedName(value="minimal_blood_pressure")
    private String minimalBloodPressure;
    @SerializedName(value="heart_rate_per_minute")
    private String heartRatePerMinute;
    @SerializedName(value="created_at")
    private String createdAt;

    public String getMaximalBloodPressure() {
        return maximalBloodPressure;
    }

    public void setMaximalBloodPressure(String maximalBloodPressure) {
        this.maximalBloodPressure = maximalBloodPressure;
    }

    public String getMinimalBloodPressure() {
        return minimalBloodPressure;
    }

    public void setMinimalBloodPressure(String minimalBloodPressure) {
        this.minimalBloodPressure = minimalBloodPressure;
    }

    public String getHeartRatePerMinute() {
        return heartRatePerMinute;
    }

    public void setHeartRatePerMinute(String heartRatePerMinute) {
        this.heartRatePerMinute = heartRatePerMinute;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
