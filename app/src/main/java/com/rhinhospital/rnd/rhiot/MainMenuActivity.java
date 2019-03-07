package com.rhinhospital.rnd.rhiot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import info.hoang8f.widget.FButton;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainMenuActivity";
    private FButton btnPatientQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String nurse_name = pref.getString("nurse_name","");
        Log.d(TAG, nurse_name);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(nurse_name);
        btnPatientQRCode = (FButton)findViewById(R.id.btnPatientQRCode);
        btnPatientQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPatientQRCode:
//                Intent intent = new Intent(MainMenuActivity.this, MeasureTypeActivity.class);
//                startActivity(intent);
//                new IntentIntegrator(this).initiateScan();
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setCaptureActivity( ZxingActivity.class );
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
                break;
        }
    }

    // QR 코드 결과값 처리는 메소드
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        //  com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
        //  = 0x0000c0de; // Only use bottom 16 bits
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                // 취소됨
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // 스캔된 QRCode --> result.getContents()
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String[] patientInfo;

//                String strResult = result.getContents();
//
//                String[] array = strResult.split(",");
//                for(int i = 0; i< strResult.length(); i++){
//                    String[] info = array[i].split(":");
//                    //RhinLog.print(String.valueOf(i) + " = " + info[0] +" , " + info[1]);
//
//
//                    //edtPassword.setText();
//                }
                Intent intent = new Intent(this, MeasureTypeActivity.class);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
