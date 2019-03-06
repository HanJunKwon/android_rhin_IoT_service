package com.rhinhospital.rnd.rhiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rhinhospital.rnd.rhiot.Result.Department;
import com.rhinhospital.rnd.rhiot.Result.Login;
import com.rhinhospital.rnd.rhiot.RetrofitAPI.RetrofitService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NurseRegistActivity extends AppCompatActivity {
    private final static String TAG = "NURSEREGISTACTIVITY";

    private Spinner spNurseRegistBirthY, spNurseRegistBirthM, spNurseRegistBirthD, spDepartment, spPosition;
    private List<String> listY, listM, listD;
    private HashMap<String, String> hashDepartment, hashPosition;
    private ArrayAdapter spNurseRegistBirthYAdapter, spNurseRegistBirthMAdapter, spNurseRegistBirthDAdapter, spDepartmentAdapter, spPositionAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_regist);

        // 생년월일 스피너 inflate
        spNurseRegistBirthY = (Spinner) findViewById(R.id.spNurseRegistBirthY);
        spNurseRegistBirthM = (Spinner) findViewById(R.id.spNurseRegistBirthM);
        spNurseRegistBirthD = (Spinner) findViewById(R.id.spNurseRegistBirthD);

        // 1960년 부터 현재 년도까지
        listY = new ArrayList<String>();
        for(int y = 1960; y<=Calendar.getInstance().get(Calendar.YEAR); y++){
            listY.add(Integer.toString(y));
        }
        // 1월부터 12월까지
        listM = new ArrayList<String>();
        for(int m = 1; m <= 12; m++){
            listM.add(Integer.toString(m));
        }
        // 1일부터 31일까지
        listD = new ArrayList<String>();
        for(int d = 1; d <= 31; d++){
            listD.add(Integer.toString(d));
        }

        // 생년월일 스피너 어댑터 생성
        spNurseRegistBirthYAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listY);
        spNurseRegistBirthMAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listM);
        spNurseRegistBirthDAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listD);

        spNurseRegistBirthY.setAdapter(spNurseRegistBirthYAdapter);
        spNurseRegistBirthM.setAdapter(spNurseRegistBirthMAdapter);
        spNurseRegistBirthD.setAdapter(spNurseRegistBirthDAdapter);


        // 부서정보 스피너
        spDepartment = (Spinner) findViewById(R.id.spDepartment);
        Call<Department> getDepartmentCall = RetrofitService.getInstance().getService().getDepartmentList();
        getDepartmentCall.enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                Log.d(TAG, "onResponse");
                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Toast.makeText(NurseRegistActivity.this, "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show();

            }
        });


        // HashMap Value => List로 변환해서 값만 넣어주는 로직 필요함
        //spDepartmentAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, hashDepar);
        //spDepartment.setAdapter();
        // 네트워크로 부서 정보 받아옴


        // DB에서 직위 정보 받아옴
        spPosition = (Spinner) findViewById(R.id.spPosition);
        //spPosition.setAdapter();


    }
}
