package com.rhinhospital.rnd.rhiot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import info.hoang8f.widget.FButton;

public class MeasureTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MeasureTypeActivity";
    FButton fbtnBloodPressure, fbtnMeasureHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_type);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String nurse_name = pref.getString("nurse_name","");

        Log.d(TAG, nurse_name);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(nurse_name+"/권한준 26세 남");

        fbtnBloodPressure = (FButton) findViewById(R.id.btnBloodPressure);
        fbtnBloodPressure.setOnClickListener(this);

        fbtnMeasureHistory = (FButton) findViewById(R.id.btnMeasureHistory);
        fbtnMeasureHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnBloodPressure:
                intent = new Intent(this, IoTServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMeasureHistory:
                intent = new Intent(this, MeasureHistoryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
