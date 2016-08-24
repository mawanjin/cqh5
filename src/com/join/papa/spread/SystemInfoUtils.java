package com.join.papa.spread;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by lala on 16/8/20.
 */
public class SystemInfoUtils {
    static String TAG = SystemInfoUtils.class.getSimpleName();
    Context mContext;
    static SystemInfoUtils systemInfoUtils;

    private SystemInfoUtils(Context context) {
        mContext = context;
    }

    public static SystemInfoUtils getInstance(Context context) {
        if (systemInfoUtils == null)
            systemInfoUtils = new SystemInfoUtils(context);
        return systemInfoUtils;
    }


    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public String getInfo() {
        TelephonyManager mTm = (TelephonyManager) mContext
                .getSystemService(mContext.TELEPHONY_SERVICE);

        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb = android.os.Build.BRAND;// 手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得

        return "[version:" + getVersionName() + ",imei:" + imei + ",imsi:"
                + imsi + ",model:" + mtype + ",brand:" + mtyb + ",number:"
                + numer + "]";
    }

    public String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                    0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        String version = packInfo.versionName;
        return version;
    }
    /**
     * device factory name, e.g: Samsung
     * 手机品牌
     * @return the vENDOR
     */
    public String getVendor() {
        return Build.BRAND;
    }
    public int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        int version = packInfo.versionCode;
        return version;
    }

    /**
     * versionCode versionName
     *
     * @return
     */
    public String[] getVersionNameArray() {
        String[] a = new String[2];
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return a;
        }
        a[0] = packInfo.versionCode + "";
        a[1] = packInfo.versionName;
        return a;
    }

    static String mac;

    /**
     * .获取手机MAC地址 只有手机开启wifi才能获取到mac地址
     */
    public String getMacAddress() {
        String result = "";
        try {
            if (imei!=null&&!imei.equals("")) return mac;
            WifiManager wifiManager = (WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
            mac = result;
            Log.i("text", "手机macAdd:" + result);
            if (mac == null || mac.equals("")) mac = getIDFinal();
        }catch (Exception e){
            if (mac == null || mac.equals("")) mac = getIDFinal();
        }
        return result;
    }

    public static String getIDFinal(){
        String m_szDevIDShort="35";
        try{
            m_szDevIDShort = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length()%10 +
                    Build.BRAND.length()%10 +
                    Build.CPU_ABI.length()%10 +
                    Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 +
                    Build.HOST.length()%10 +
                    Build.ID.length()%10 +
                    Build.MANUFACTURER.length()%10 +
                    Build.MODEL.length()%10 +
                    Build.PRODUCT.length()%10 +
                    Build.TAGS.length()%10 +
                    Build.TYPE.length()%10 +
                    Build.USER.length()%10 ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return m_szDevIDShort;
    }



    /**
     *获取手机型号
     */
    public String getMobileMODEL(){
        return Build.MODEL;
    }
    /**
     *获取手机系统版本
     */
    public String getMobileSystenmVersion(){
        return Build.VERSION.RELEASE;
    }

    static String imei="";

    public String getDeviceId() {
        try{
            if (imei==null||imei.equals("")||imei.equals("0")) {
                TelephonyManager tm = (TelephonyManager) mContext
                        .getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm.getDeviceId();
                if (imei==null||imei.equals("")||imei.equals("0"))
                    imei = getMacAddress().replaceAll(":", "_");
            }
        }catch (Exception e){
            imei = getMacAddress().replaceAll(":", "_");
        }
        return imei;
    }

    /**
     * 检测是否已安装
     *
     * @param pak 包名
     * @return
     */
    public boolean checkInstall(Context context, String pak) {
        boolean install = false;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pak, 1);
            if (info != null && info.activities.length > 0) {
                install = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
        }
        return install;
    }

    String qudao;

    /**
     * 获取渠道号
     *
     * @param context
     * @return
     * @throws android.content.pm.PackageManager.NameNotFoundException
     */
    public  String getAd(Context context)  {

        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            int key = appInfo.metaData.getInt("PA_AD_ID");
            qudao = key+"";
            return qudao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2622";
    }



}
