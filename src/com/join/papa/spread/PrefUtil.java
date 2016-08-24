package com.join.papa.spread;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by lala on 15/7/20.
 */
public class PrefUtil {
    SharedPreferences mSharedPreferences;
    static PrefUtil prefUtil;
    Context context;

    private PrefUtil(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences("Papa_Stat_SharedPreferences", Context.MODE_PRIVATE);
    }

    public static PrefUtil getInstance(Context context) {
        if (prefUtil == null) {
            prefUtil = new PrefUtil(context);
        }
        return prefUtil;
    }

    public String getAccountVer2(){
        return mSharedPreferences.getString("accountVer2", "");
    }

    public void setAccountVer2(String val) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("accountVer2", val);
        mEditor.commit();
    }
}
