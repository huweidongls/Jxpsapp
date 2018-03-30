package com.example.zsccpsapp.jxpsapp.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.Bean.DaytjBean;
import com.example.zsccpsapp.jxpsapp.Bean.TongJiBean;
import com.example.zsccpsapp.jxpsapp.R;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.HttpUtil;
import com.example.zsccpsapp.jxpsapp.Utils.L;
import com.example.zsccpsapp.jxpsapp.Utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

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

public class SrtjActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_starttimte)
    TextView tvStarttime;
    @BindView(R.id.tv_endtimte)
    TextView tvEndtime;
    @BindView(R.id.tv_search)
    ImageView tvSearch;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.daydet)
    TextView daydet;
    @BindView(R.id.daynum)
    TextView daynum;
    @BindView(R.id.sales_ll2)
    LinearLayout salesLl2;
    @BindView(R.id.week)
    TextView week;
    @BindView(R.id.sales_ll4)
    LinearLayout salesLl4;
    @BindView(R.id.weeknum)
    TextView weeknum;
    @BindView(R.id.sales_ll5)
    LinearLayout salesLl5;
    @BindView(R.id.sta_ll2)
    LinearLayout staLl2;
    @BindView(R.id.mon)
    TextView mon;
    @BindView(R.id.monnum)
    TextView monnum;
    @BindView(R.id.sta_ll3)
    LinearLayout staLl3;
    @BindView(R.id.ll_sb)
    LinearLayout llSb;

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private String uid;
    private String pass;
    private static final String TAG = "SrtjActivity";
    private String dayCost;
    private String dayNum;
    private String weekCost;
    private String weekNum;
    private String monCost;
    private String monNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        MyApp.getInstance().addActivity(SrtjActivity.this);

        ButterKnife.bind(this);

        init();

    }

    private void init() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);//获取当前年份
        monthOfYear = calendar.get(Calendar.MONTH) + 1;//获取当前月份
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);//获取当前日期
        tvStarttime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        tvEndtime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        SharedPreferences pref = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE);
        uid = L.decrypt(pref.getString("uid", ""), Consts.LKEY);
        pass = L.decrypt(pref.getString("pwd", ""), Consts.LKEY);
        loadData();

    }

    /**
     * 网络请求数据
     */
    private void loadData() {

        final String url = "/index.php?ctrl=psuser&action=psyapptj&datatype=json&uid=" + uid + "&pwd=" + pass;
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
                        Log.d(TAG, "onResponse: " + result);
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
                initView(value);
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

    /**
     * 加载页面数据
     */
    private void initView(String value) {

        try {
            JSONObject jsonObject = new JSONObject(value);
            String error = jsonObject.getString("error");
            if (error.equals("false")) {
                TongJiBean tongJiBean = new TongJiBean();
                Gson gson = new Gson();
                tongJiBean = gson.fromJson(value, TongJiBean.class);
                dayCost = tongJiBean.getMsg().getDayccost() + "";
                dayNum = tongJiBean.getMsg().getDayshuliang() + "";
                weekCost = tongJiBean.getMsg().getWeektccost() + "";
                weekNum = tongJiBean.getMsg().getWeekshuliang() + "";
                monCost = tongJiBean.getMsg().getMonthccost() + "";
                monNum = tongJiBean.getMsg().getMonthshuliang() + "";
                day.setText(dayCost);
                daynum.setText(dayNum);
                week.setText(weekCost);
                weeknum.setText(weekNum);
                mon.setText(monCost);
                monnum.setText(monNum);
            } else {
                ToastUtil.showShort(SrtjActivity.this, "数据请求失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.ll_sb, R.id.back, R.id.tv_starttimte, R.id.tv_endtimte, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                SrtjActivity.this.finish();
                break;
            case R.id.tv_starttimte:
                new DatePickerDialog(SrtjActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvStarttime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, monthOfYear, dayOfMonth).show();
                break;
            case R.id.tv_endtimte:
                new DatePickerDialog(SrtjActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvEndtime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, monthOfYear, dayOfMonth).show();
                break;
            case R.id.tv_search:
                search();
                break;
            case R.id.ll_sb:
                llSb.setVisibility(View.GONE);
                staLl2.setVisibility(View.VISIBLE);
                staLl3.setVisibility(View.VISIBLE);
                daydet.setText("今日收入");
                day.setText(dayCost);
                daynum.setText(dayNum);
                break;
        }
    }

    private void search() {

        String startTime = tvStarttime.getText().toString();
        String endTime = tvEndtime.getText().toString();
        final String url = "/index.php?ctrl=psuser&action=daytj&datatype=json&uid=" + uid + "&pwd=" + pass + "&startdate=" + startTime + "&enddate=" + endTime;
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
                DaytjBean daytjBean = new DaytjBean();
                Gson gson = new Gson();
                daytjBean = gson.fromJson(value, DaytjBean.class);
                Log.d(TAG, "onNext: " + value);
                staLl2.setVisibility(View.GONE);
                staLl3.setVisibility(View.GONE);
                daydet.setText("收入");
                day.setText(daytjBean.getMsg().getTccost() + "");
                daynum.setText(daytjBean.getMsg().getShuliang() + "");
                llSb.setVisibility(View.VISIBLE);
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

}
