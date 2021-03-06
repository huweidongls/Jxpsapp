package com.example.zsccpsapp.jxpsapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zsccpsapp.jxpsapp.Activity.ShowOrderActivity;
import com.example.zsccpsapp.jxpsapp.Adapter.DqdAdapter;
import com.example.zsccpsapp.jxpsapp.Entity.DqdEntity;
import com.example.zsccpsapp.jxpsapp.R;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.HttpUtil;
import com.example.zsccpsapp.jxpsapp.Utils.L;
import com.example.zsccpsapp.jxpsapp.Utils.ToastUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 99zan on 2018/2/7.
 */

public class DqdFragment extends Fragment {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<DqdEntity> data = new ArrayList<>();
    private DqdAdapter adapter;

    private String uid;
    private String pass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dqd_fragment, null);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {

        refreshLayout.setRefreshHeader(new MaterialHeader(getContext()).setShowBezierWave(true));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });

        SharedPreferences pref = getContext().getSharedPreferences(Consts.FILENAME, MODE_PRIVATE);
        uid = L.decrypt(pref.getString("uid", ""), Consts.LKEY);
        pass = L.decrypt(pref.getString("pwd", ""), Consts.LKEY);

//        getData();
        setData();

    }

    private void getData() {

        final String url = "/index.php?ctrl=psuser&action=waitorder&uid=" + uid + "&pwd=" + pass + "&datatype=json";
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

                try {
                    JSONObject jsonObject = new JSONObject(value);
                    String error = jsonObject.getString("error");
                    if(error.equals("true")){
                        ToastUtil.showShort(getContext(), "数据请求失败！");
                    }else{
                        setData();
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

    private void setData() {

        data.add(new DqdEntity());
        data.add(new DqdEntity());
        data.add(new DqdEntity());
        data.add(new DqdEntity());
        data.add(new DqdEntity());
        adapter = new DqdAdapter(getContext(), data);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showShort(getContext(), "点击了item"+position);
                Intent intent = new Intent();
                intent.setClass(getContext(), ShowOrderActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new DqdAdapter.OnItemClickListener() {
            @Override
            public void onClick(int i) {
                ToastUtil.showShort(getContext(), "点击了button"+i);
                new AlertDialog.Builder(getContext())
                        .setTitle("确认抢单")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage("请仔细查看订单信息，抢单成功后务必按照平台规定完成配送。")
                        .setPositiveButton("确认抢单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

}
