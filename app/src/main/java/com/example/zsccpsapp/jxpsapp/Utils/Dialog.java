package com.example.zsccpsapp.jxpsapp.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.zsccpsapp.jxpsapp.R;

/**
 * Created by 99zan on 2018/2/8.
 */

public class Dialog {

    private static Dialog dialog;

    public static Dialog getInstance() {
        if (dialog == null) {
            dialog = new Dialog();
        }
        return dialog;
    }

    public void showDialog(Context context, String msg, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener1){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setIcon(R.drawable.ic_launcher)
                .setMessage(msg)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", listener1)
                .show();
    }

}
