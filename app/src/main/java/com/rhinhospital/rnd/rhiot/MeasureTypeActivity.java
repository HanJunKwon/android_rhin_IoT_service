package com.rhinhospital.rnd.rhiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import info.hoang8f.widget.FButton;

public class MeasureTypeActivity extends AppCompatActivity implements View.OnClickListener {
    FButton fbtnBloodPressure, fbtnMeasureHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_type);

        fbtnBloodPressure = (FButton) findViewById(R.id.btnBloodPressure);
        fbtnBloodPressure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBloodPressure:
                break;
            case R.id.btnMeasureHistory:
                Intent intent = new Intent(this, MeasureHistoryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
