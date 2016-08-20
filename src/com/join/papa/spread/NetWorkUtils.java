package com.join.papa.spread;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lala on 16/8/19.
 */
public class NetWorkUtils {


    private static final String TAG = "NetWorkUtils";
    /** Called when the activity is first created. */
    public static final    String CTWAP = "ctwap";
    public static final    String CMWAP = "cmwap";
    public static final    String WAP_3G = "3gwap";
    public static final    String UNIWAP = "uniwap";
    public static final    int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
    public static final    int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public static final    int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public static final    int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络

    public static void disconnect(Context context) {
        if (context != null) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            mWifiManager.disconnect();
        }
    }

    public static String getSSID(Context context) {
        if (context != null) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return mWifiManager.getConnectionInfo().getSSID();
        }
        return "";
    }

    public static String getIP(Context context) {
        if (context != null) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return intToIp(mWifiManager.getConnectionInfo().getIpAddress());
        }
        return "0.0.0.0";
    }

    public static String intToIp(int paramIntip) {
        return (paramIntip & 0xFF) + "." + ((paramIntip >> 8) & 0xFF) + "."
                + ((paramIntip >> 16) & 0xFF) + "." + ((paramIntip >> 24) & 0xFF);
    }

    // 判断网络连接状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            try{
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager
                        .getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    // 判断wifi状态
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
//                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (mWiFiNetworkInfo != null) {
//                return (mWiFiNetworkInfo.isAvailable());
//            }
            NetworkInfo.State wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            //判断为wifi状态
            if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
                NetworkInfo activeNetInfo = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetInfo != null
                        && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        }
        return false;
    }

    // 判断移动网络
    public static boolean isMobileConnected(Context context) {
        try{
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo.State mobile =mMobileNetworkInfo.getState();
                if(mobile== NetworkInfo.State.CONNECTED||mobile== NetworkInfo.State.CONNECTING)
                    return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // 获取连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /* @author sichard
    * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
            * @return
            */
    public static final boolean ping() {
        Log.d(TAG,"method ping() called.");
        int status,count=3;
        URL url = null;
        HttpURLConnection connection = null;

        while (count>0){
            count--;
            try {
                url = new URL("http://www.baidu.com");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(6000);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestMethod("GET");
                status = connection.getResponseCode();
                Log.d(TAG,"ping status is "+status);
                if(status==200)return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                    connection.disconnect();
            }
        }

        if(connection!=null)
            connection.disconnect();

        return false;
    }
    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     *
     * */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {


                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。

                Log.d( TAG,"=====================>无网络");
                return TYPE_OTHER_NET;
            } else {

                // NetworkInfo不为null开始判断是网络类型

                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    Log.d( TAG,"=====================>wifi网络");
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {


                    // 注意二：
                    // 判断是否电信wap:
                    //不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
//google已经禁用权限了所以这段用不着了。
//                    final Cursor c = mContext.getContentResolver().query(
//                            PREFERRED_APN_URI, null, null, null, null);
//                    if (c != null) {
//                        c.moveToFirst();
//                        final String user = c.getString(c
//                                .getColumnIndex("user"));
//                        if (!TextUtils.isEmpty(user)) {
//                            LogUtil_.logError(
//                                    "=====================>代理："
//                                            + c.getString(c
//                                            .getColumnIndex("proxy")));
//                            if (user.startsWith(CTWAP)) {
//                                LogUtil_.logError( "=====================>电信wap网络");
//                                return TYPE_CT_WAP;
//                            }
//                        }
//                    }
//                    c.close();
                    String proxyHost=android.net.Proxy.getDefaultHost();
                    if ("10.0.0.172".equals(proxyHost)){
                        Log.d(TAG, "netMode ================== 10.0.0.172" );
                        return TYPE_CM_CU_WAP;
                    }
                    if ("10.0.0.200".equals(proxyHost)){
                        Log.d(TAG, "netMode ================== 10.0.0.200");
                        return TYPE_CT_WAP;
                    }
                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    //来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    //实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    Log.d(TAG, "netMode ================== " + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode=netMode.toLowerCase();
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            Log.d(TAG, "=====================>移动联通wap网络");
                            return TYPE_CM_CU_WAP;
                        }

                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }
        Log.d(TAG,"net 网络");
        return TYPE_OTHER_NET;

    }
}
