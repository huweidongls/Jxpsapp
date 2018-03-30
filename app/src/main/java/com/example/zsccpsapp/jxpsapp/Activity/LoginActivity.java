package com.example.zsccpsapp.jxpsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.Bean.LoginBean;
import com.example.zsccpsapp.jxpsapp.MainActivity;
import com.example.zsccpsapp.jxpsapp.R;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.HttpUtil;
import com.example.zsccpsapp.jxpsapp.Utils.L;
import com.example.zsccpsapp.jxpsapp.Utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 99zan on 2018/2/6.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.button1)
    Button button1;

    String userName;
    String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginnew);

        MyApp.getInstance().addActivity(LoginActivity.this);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.button1})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button1:
                login();
                break;
        }
    }

    private void login() {

        userName = editText1.getText().toString();
        passWord = editText2.getText().toString();
        final String url = "/index.php?ctrl=psuser&action=applogin&uname=" + userName + "&pwd=" + passWord + "&datatype=json";
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                HttpUtil.getInstance().get(url, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body().string();
                        e.onNext(result);
                        e.onComplete();
                    }
                });
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(value);
                    String error = jsonObject.getString("error");
                    if (error.equals("false")) {
                        Gson gson = new Gson();
                        LoginBean loginBean = new LoginBean();
                        loginBean = gson.fromJson(value, LoginBean.class);
                        Log.e("111", "uid=" + loginBean.getMsg().getUid() + "......pwd=" + loginBean.getMsg().getUsername());
                        saveMsg(loginBean);
                    } else {
                        try {
                            ToastUtil.showShort(LoginActivity.this, jsonObject.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    private void saveMsg(LoginBean loginBean) {
        Intent intent = new Intent();
        SharedPreferences.Editor editor = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE).edit();
        editor.putString("username", L.encrypt(userName, Consts.LKEY));
        editor.putString("pwd", L.encrypt(passWord, Consts.LKEY));
        editor.putString("uid", L.encrypt(loginBean.getMsg().getUid(), Consts.LKEY));
        editor.putString("phone", L.encrypt(loginBean.getMsg().getPhone(), Consts.LKEY));
        editor.putString("email", L.encrypt(loginBean.getMsg().getEmail(), Consts.LKEY));
        editor.putString("creattime", L.encrypt(loginBean.getMsg().getCreattime(), Consts.LKEY));
        editor.putString("logintime", L.encrypt(loginBean.getMsg().getLogintime(), Consts.LKEY));
        editor.putString("usertype", L.encrypt(loginBean.getMsg().getUsertype(), Consts.LKEY));
        editor.putString("score", L.encrypt(loginBean.getMsg().getScore(), Consts.LKEY));
        editor.putString("cost", L.encrypt(loginBean.getMsg().getCost(), Consts.LKEY));
        editor.putString("loginip", L.encrypt(loginBean.getMsg().getLoginip(), Consts.LKEY));
        editor.putString("shopid", L.encrypt(loginBean.getMsg().getShopid(), Consts.LKEY));
        editor.putString("shopcost", L.encrypt(loginBean.getMsg().getShopcost(), Consts.LKEY));
        editor.putString("isvip", L.encrypt(loginBean.getMsg().getIsvip(), Consts.LKEY));
        editor.commit();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        LoginActivity.this.finish();
    }

}
