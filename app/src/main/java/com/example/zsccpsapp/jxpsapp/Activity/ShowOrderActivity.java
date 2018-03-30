package com.example.zsccpsapp.jxpsapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.R;

import butterknife.ButterKnife;

public class ShowOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetnew);
        ButterKnife.bind(this);
        MyApp.getInstance().addActivity(ShowOrderActivity.this);
    }
}
