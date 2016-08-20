package com.join.papa.spread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by lala on 16/8/19.
 */
public class ToastUtils {
    Toast toastStart;
    Handler mHandler;
    private static ToastUtils ourInstance;

    public static ToastUtils getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ToastUtils(context);

        }
        return ourInstance;
    }

    private ToastUtils(Context context) {
        toastStart = new Toast(context);
        toastStart = Toast.makeText(context, "", Toast.LENGTH_LONG);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    toastStart.setText((CharSequence) msg.obj);
                    toastStart.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void showToastSystem(String info) {
        Message msg = mHandler.obtainMessage();
        msg.obj = info;
        msg.sendToTarget();
    }
}
