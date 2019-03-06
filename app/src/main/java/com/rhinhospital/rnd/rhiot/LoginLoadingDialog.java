package com.rhinhospital.rnd.rhiot;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.victor.loading.newton.NewtonCradleLoading;

public class LoginLoadingDialog extends Dialog {
    private NewtonCradleLoading newtonCradleLoading;

    public LoginLoadingDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_loading_dialog);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
    }

    public void executeLoad(){
        if(newtonCradleLoading.isStart()){
            newtonCradleLoading.stop();
        } else{
            newtonCradleLoading.start();
        }
    }
}
