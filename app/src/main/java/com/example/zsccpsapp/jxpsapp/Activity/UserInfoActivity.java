package com.example.zsccpsapp.jxpsapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.R;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.Dialog;
import com.example.zsccpsapp.jxpsapp.Utils.HttpUtil;
import com.example.zsccpsapp.jxpsapp.Utils.L;
import com.example.zsccpsapp.jxpsapp.Utils.ToastUtil;

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

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.detusername)
    TextView detusername;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.modifypwd)
    TextView modifypwd;
    @BindView(R.id.exit)
    TextView exit;

    private LayoutInflater inflater;
    private View dialogView;
    private EditText etOldpwd;
    private EditText etNewpwd;
    private EditText etSurepwd;
    private static final String TAG = "UserInfoActivity";
    private AlertDialog dialog;
    private String name;
    private String phoneNum;
    private String uid;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.right_userinfonew);

        MyApp.getInstance().addActivity(UserInfoActivity.this);

        ButterKnife.bind(this);
        
        init();
        
    }

    private void init() {

        SharedPreferences sp = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE);
        uid = L.decrypt(sp.getString("uid",""), Consts.LKEY);
        pass = L.decrypt(sp.getString("pwd",""), Consts.LKEY);
        name = L.decrypt(sp.getString("username",""), Consts.LKEY);
        phoneNum = L.decrypt(sp.getString("phone",""), Consts.LKEY);
        detusername.setText(name);
        phone.setText(phoneNum);

        inflater = LayoutInflater.from(this);
        dialogView = inflater.inflate(R.layout.modifypwd, null);
        etOldpwd = dialogView.findViewById(R.id.oldpwd);
        etNewpwd = dialogView.findViewById(R.id.newpwd);
        etSurepwd = dialogView.findViewById(R.id.surepwd);
        showDialog();

    }

    @OnClick({R.id.back, R.id.modifypwd, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                UserInfoActivity.this.finish();
                break;
            case R.id.modifypwd:
                etOldpwd.setText("");
                etNewpwd.setText("");
                etSurepwd.setText("");
                etOldpwd.requestFocus();
                dialog.show();
                break;
            case R.id.exit:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog() {

        Dialog.getInstance().showDialog(UserInfoActivity.this, "是否退出登录？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSp();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtil.showShort(UserInfoActivity.this, "quxiao");
            }
        });

    }

    /**
     * 清空本地配置文件并退出APP
     */
    private void clearSp() {

        SharedPreferences.Editor editor = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        MyApp.getInstance().exit();

    }

    private void showDialog() {

        dialog = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("修改密码")
                .setIcon(R.drawable.ic_launcher)
                .setView(dialogView)
                .setPositiveButton("提交保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifypwd();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();

    }

    private void modifypwd() {

        String oldpwd = etOldpwd.getText().toString();
        String newpwd = etNewpwd.getText().toString();
        final String surepwd = etSurepwd.getText().toString();
        final String url = "/index.php?ctrl=psuser&action=modify&uid=" +uid+ "&pwd=" +pass+ "&oldpwd=" +oldpwd+ "&newpwd=" +newpwd+ "&surepwd=" +surepwd+ "&datatype=json";
        Log.d(TAG, "modifypwd: "+oldpwd+newpwd+surepwd);
        if(!oldpwd.equals(pass)){
            ToastUtil.showShort(UserInfoActivity.this, "旧密码错误");
        }else if(!newpwd.equals(surepwd)){
            ToastUtil.showShort(UserInfoActivity.this, "新密码不一致");
        }else if(TextUtils.isEmpty(newpwd)||TextUtils.isEmpty(surepwd)){
            ToastUtil.showShort(UserInfoActivity.this, "新密码不能为空");
        }else{
            Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(final ObservableEmitter<String> e) throws Exception {
                    HttpUtil.getInstance().get(url, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
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

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    ToastUtil.showShort(UserInfoActivity.this, "修改成功，请重新登录！");
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    UserInfoActivity.this.finish();
                    dialog.dismiss();
                }
            };
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }

    }

}
