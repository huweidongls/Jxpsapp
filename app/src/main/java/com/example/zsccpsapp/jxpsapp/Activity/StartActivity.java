package com.example.zsccpsapp.jxpsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.MainActivity;
import com.example.zsccpsapp.jxpsapp.R;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.L;

public class StartActivity extends AppCompatActivity {

    String uid;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        MyApp.getInstance().addActivity(StartActivity.this);

//        saveMessage();
        getMessage();

    }

    private void getMessage() {
        Intent intent = new Intent();
        SharedPreferences pref = getSharedPreferences(Consts.FILENAME,MODE_PRIVATE);
        uid= L.decrypt(pref.getString("uid",""), Consts.LKEY);
        pwd= L.decrypt(pref.getString("pwd",""), Consts.LKEY);
        if (TextUtils.isEmpty(uid)){
            intent.setClass(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            StartActivity.this.finish();
        }else{
            intent.setClass(StartActivity.this, MainActivity.class);
            startActivity(intent);
            StartActivity.this.finish();
        }
    }

    private void saveMessage() {
        SharedPreferences.Editor editor = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE).edit();
        editor.putString("uid", L.encrypt(uid, Consts.LKEY));
        editor.putString("pwd", L.encrypt(pwd, Consts.LKEY));
        editor.commit();
    }

}
