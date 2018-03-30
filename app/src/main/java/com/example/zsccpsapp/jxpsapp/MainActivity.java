package com.example.zsccpsapp.jxpsapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zsccpsapp.jxpsapp.Activity.MapActivity;
import com.example.zsccpsapp.jxpsapp.Activity.SrtjActivity;
import com.example.zsccpsapp.jxpsapp.Activity.UserInfoActivity;
import com.example.zsccpsapp.jxpsapp.App.MyApp;
import com.example.zsccpsapp.jxpsapp.Fragment.DqdFragment;
import com.example.zsccpsapp.jxpsapp.Fragment.DqhFragment;
import com.example.zsccpsapp.jxpsapp.Fragment.DwcFragment;
import com.example.zsccpsapp.jxpsapp.Service.IntentService;
import com.example.zsccpsapp.jxpsapp.Service.PushService;
import com.example.zsccpsapp.jxpsapp.Utils.Consts;
import com.example.zsccpsapp.jxpsapp.Utils.Dialog;
import com.example.zsccpsapp.jxpsapp.Utils.L;
import com.example.zsccpsapp.jxpsapp.Utils.ToastUtil;
import com.igexin.sdk.PushManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.open)
    ImageView open;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.menu_shoplist)
    TextView menuShoplist;
    @BindView(R.id.menu_orderslist)
    TextView menuOrderslist;
    @BindView(R.id.menu_about)
    TextView menuAbout;
    @BindView(R.id.menu_userinfo)
    TextView menuUserinfo;
    @BindView(R.id.menu_contact)
    TextView menuContact;
    @BindView(R.id.menu_out)
    TextView menuOut;
    @BindView(R.id.title_bar_menu_btn)
    ImageView titleBarMenuBtn;
    @BindView(R.id.title_bar_name)
    TextView titleBarName;
    @BindView(R.id.neworder)
    TextView neworder;
    @BindView(R.id.wait)
    TextView wait;
    @BindView(R.id.ing)
    TextView ing;
    @BindView(R.id.username)
    TextView username;

    private String name;
    private List<Fragment> fragmentList = new ArrayList<>();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApp.getInstance().addActivity(MainActivity.this);

        ButterKnife.bind(this);

        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);
        
        init();
        
    }

    private void init() {

        SharedPreferences sp = getSharedPreferences(Consts.FILENAME, MODE_PRIVATE);
        name = L.decrypt(sp.getString("username",""), Consts.LKEY);
        username.setText(name);
        titleBarName.setText("待抢订单");
        titleBarMenuBtn.setImageResource(R.drawable.btnbar_menu);
        Fragment dqdFragment = new DqdFragment();
        Fragment dqhFragment = new DqhFragment();
        Fragment dwcFragment = new DwcFragment();
        fragmentList.add(dqdFragment);
        fragmentList.add(dqhFragment);
        fragmentList.add(dwcFragment);

        fragmentTransaction.add(R.id.fl,dqdFragment);
        fragmentTransaction.add(R.id.fl,dqhFragment);
        fragmentTransaction.add(R.id.fl,dwcFragment);

        fragmentTransaction.show(dqdFragment).hide(dqhFragment).hide(dwcFragment);
        fragmentTransaction.commit();

        selectButton(neworder);

    }

    @OnClick({R.id.neworder, R.id.wait, R.id.ing, R.id.title_bar_menu_btn, R.id.open, R.id.close, R.id.menu_shoplist, R.id.menu_about, R.id.menu_userinfo, R.id.menu_out})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.neworder:
                selectButton(neworder);
                titleBarName.setText("待抢订单");
                switchFragment(0);
                break;
            case R.id.wait:
                selectButton(wait);
                titleBarName.setText("待取订单");
                switchFragment(1);
                break;
            case R.id.ing:
                selectButton(ing);
                titleBarName.setText("待送订单");
                switchFragment(2);
                break;
            case R.id.title_bar_menu_btn:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.open:
                break;
            case R.id.close:
                break;
            case R.id.menu_shoplist:
                intent.setClass(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                intent.setClass(MainActivity.this, SrtjActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_userinfo:
                intent.setClass(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_out:
                showExitDialog();
                break;
        }
    }

    private void setDrawerLeftEdgeSize (Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mLeftDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void showExitDialog() {

        Dialog.getInstance().showDialog(MainActivity.this, "是否退出登录？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSp();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtil.showShort(MainActivity.this, "quxiao");
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


    /**
     * 选择隐藏与显示的Fragment
     * @param index 显示的Frgament的角标
     */
    private void switchFragment(int index){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        for(int i = 0; i < fragmentList.size(); i++){
            if (index == i){
                fragmentTransaction.show(fragmentList.get(index));
            }else {
                fragmentTransaction.hide(fragmentList.get(i));
            }
        }
        fragmentTransaction.commit();
    }

    /**
     * 控制底部菜单按钮的选中
     * @param v
     */
    public void selectButton(TextView v) {
        neworder.setBackgroundColor(0xffe50b0b);
        neworder.setTextColor(0xffffffff);
        wait.setBackgroundColor(0xffe50b0b);
        wait.setTextColor(0xffffffff);
        ing.setBackgroundColor(0xffe50b0b);
        ing.setTextColor(0xffffffff);
        v.setBackgroundColor(0xffffffff);
        v.setTextColor(0xffe50b0b);
    }

    @Override
    public void onBackPressed() {
        // TODO 自动生成的方法存根
        backtrack();
    }

    /**
     * 退出销毁所有activity
     */
    private void backtrack() {
        if(System.currentTimeMillis() - exitTime > 2000){
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            MyApp.getInstance().exit();
            exitTime = 0;
        }
    }

}
