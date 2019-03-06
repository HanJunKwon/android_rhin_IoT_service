package com.rhinhospital.rnd.rhiot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rhinhospital.rnd.rhiot.Model.Nurse;
import com.rhinhospital.rnd.rhiot.Result.Login;
import com.rhinhospital.rnd.rhiot.RetrofitAPI.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private final static String EXTRAS_ENDLESSS_MODE = "EXTRAS_ENDLESS_MODE";
    private EditText edtEmpNo, edtPassword;
    private Button btnSignIn, btnNurseRegist, btnQrCodeLogin;
    private MaterialTextField mtfEmpNo, mtfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmpNo = (EditText) findViewById(R.id.edtEmpNo);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        mtfEmpNo = (MaterialTextField) findViewById(R.id.mtfEmpNo);
        mtfPassword = (MaterialTextField) findViewById(R.id.mtfPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

        btnNurseRegist = (Button) findViewById(R.id.btnNurseRegist);
        btnNurseRegist.setOnClickListener(this);

        btnQrCodeLogin = (Button)findViewById(R.id.btnQrcodeLogin);
        btnQrCodeLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnQrcodeLogin:
                startQrCode();
                break;
            case R.id.btnSignIn:
                btnSignIn.setEnabled(false);
                edtEmpNo.setEnabled(false);
                edtPassword.setEnabled(false);
                mtfEmpNo.setEnabled(false);
                mtfPassword.setEnabled(false);
                btnNurseRegist.setEnabled(false);

                // 로딩 실행
                LoadingTask loadingTask = new LoadingTask();
                loadingTask.execute();

                break;
            case R.id.btnNurseRegist:
                Intent intent = new Intent(this, NurseRegistActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void startQrCode() {
        new IntentIntegrator(this).initiateScan();
    }

    // QrCode 결과값 받는부분
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
                edtEmpNo.setText("0");
                // 로딩 실행
                LoadingTask loadingTask = new LoadingTask();
                loadingTask.execute();


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    // 로그인 시 로딩 화면 처리
    private class LoadingTask extends AsyncTask<Void, Void, Void>{
        LoginLoadingDialog loginLoadingDialog = new LoginLoadingDialog(MainActivity.this);

        // 호출 순서 1
        @Override
        protected void onPreExecute() {
            loginLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                    Color.TRANSPARENT));
            loginLoadingDialog.show(); // 로딩바 보여주기
            loginLoadingDialog.executeLoad(); // 뉴턴 로딩바 좌우로 움직이게 설정

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loginLoadingDialog.dismiss(); // 로딩바 없애기
            super.onPostExecute(aVoid);
        }

        // 호출 순서 2
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                for(int i = 0; i<1; i++){
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            /**
             * 해당 사원의 비밀번호와 사원번호가 일치하는 체크 후
             * 결과의 존재 여부를 체크하는데
             * ################### (아래 내용 확이내서 추후에 쿼리 수정해야함)_권한준 연구원
             * 해당 사원번호와 비밀번호가 일치하는지 체크하는 로직과 존재 여부를 체크하는 로직을 나눠야함
             * ###################
             */
            Call<Login> loginCall = RetrofitService.getInstance().getService().login(edtEmpNo.getText().toString(), edtPassword.getText().toString());
            loginCall.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Log.d(TAG, "login onResponse");
                    Log.v(TAG, response.body().toString());
                    Login is_login = response.body();


                    if(is_login.getIs_exists().equals("1")){
                        Log.d(TAG, "login success");

                        Call<Nurse> nurseInfoCall =RetrofitService.getInstance().getService().getNurseInfo(edtEmpNo.getText().toString());
                        nurseInfoCall.enqueue(new Callback<Nurse>() {
                            @Override
                            public void onResponse(Call<Nurse> call, Response<Nurse> response) {
                                Log.d(TAG, "NurseInfo get success");
                                Log.v(TAG, response.body().toString());
                                Nurse nurse = response.body();

                                /**
                                 * App의 내부 저장소를 생성한다
                                 * 나중에 App의 모든 내부 저장소 이름은 HashMap으로 구성하여 사용하도록 변경해야 함.
                                 */
                                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("department_name", nurse.getDepartment_nm()); // 간호사 부서
                                editor.putString("position_name", nurse.getPosition_nm()); // 간호사 직급
                                editor.putString("nurse_empNo", nurse.getEmpNo()); // 간호사 사원번호
                                editor.putString("nurse_name", nurse.getNurse_nm()); // 간호사 이름
                                editor.putInt("nurse_age", nurse.getNurse_age()); // 간호사 나이
                                editor.commit();

                                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Nurse> call, Throwable t) {

                            }
                        });


                        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("nurse_name", "kwon");
                        editor.putString("nurse_empNo", "0");
                        editor.putString("nurse_age", "26");
                        editor.commit();

                        startActivity(intent);
                        finish();
                    }
                    else{
                        Log.d(TAG, "login fail");
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d(TAG, "server connected fail");
                    Toast.makeText(MainActivity.this, "서버에 접속하지 못하였습니다.", Toast.LENGTH_SHORT).show();
                    btnSignIn.setEnabled(true);
                    edtEmpNo.setEnabled(true);
                    edtPassword.setEnabled(true);
                    mtfEmpNo.setEnabled(true);
                    mtfPassword.setEnabled(true);
                    btnNurseRegist.setEnabled(true);
                }
            });

            return null;
        }
    }
}
