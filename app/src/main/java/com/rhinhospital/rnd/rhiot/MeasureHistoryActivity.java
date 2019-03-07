package com.rhinhospital.rnd.rhiot;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.rhinhospital.rnd.rhiot.Adapter.MeasurementHistoryAdapter;
import com.rhinhospital.rnd.rhiot.Result.MeasurementHistory;
import com.rhinhospital.rnd.rhiot.RetrofitAPI.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasureHistoryActivity extends AppCompatActivity {
    private final static String TAG = "MeasurementHistoryAct";
    private static ArrayList<MeasurementHistory> measurementHistoryList = new ArrayList<MeasurementHistory>();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MeasurementHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_history);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String nurse_name = pref.getString("nurse_name","");

        Log.d(TAG, nurse_name);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(nurse_name+"/권한준 26세 남");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycleMeasurementHistory);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        Call<ArrayList<MeasurementHistory>> getMeasurementHistory = RetrofitService.getInstance().getService().getMeasurementHistory("00001", 0);
        getMeasurementHistory.enqueue(new Callback<ArrayList<MeasurementHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<MeasurementHistory>> call, Response<ArrayList<MeasurementHistory>> response) {
                Log.d(TAG, "onResponse");
                Log.d(TAG, response.body().toString());
                measurementHistoryList = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<MeasurementHistory>> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, t.getMessage());
            }
        });

        historyAdapter = new MeasurementHistoryAdapter(measurementHistoryList);
        mRecyclerView.setAdapter(historyAdapter);
    }
}
