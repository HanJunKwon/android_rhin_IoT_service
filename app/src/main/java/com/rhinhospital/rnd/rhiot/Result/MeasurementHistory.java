package com.rhinhospital.rnd.rhiot.Result;

public class MeasurementHistory {
    private String maximalBloodPressure;
    private String minimalBloodPressure;
    private String heartRatePerMinute;
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
