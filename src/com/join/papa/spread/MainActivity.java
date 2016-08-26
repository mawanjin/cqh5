package com.join.papa.spread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends Activity {
    private  static String TAG = "MainActivity";
    private WebView mWebView;
    private String url;
    private LinearLayout loding_layout;
    private Handler mHandler = new Handler();
    private LinearLayout loding_faile;
    private boolean isError;
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    System.exit(0);
                }
                return true;
            }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mWebView = (WebView) findViewById(R.id.webView);
        loding_layout = (LinearLayout) findViewById(R.id.loding_layout);
        loding_faile = (LinearLayout) findViewById(R.id.loding_faile);
        findViewById(R.id.setNetwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting();
            }
        });

        initWebView();
        url = "http://sdtuis.papa91.com/h5/index/"+SystemInfoUtils.getInstance(this).getAd(this);
        lodeWebView(url);
    }


    private void initWebView() {
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClientXY(this));

    }


    public void lodeWebViewWhenClick(View view){
        lodeWebView(url);
    }

    private void lodeWebView(String url) {
        Log.d(TAG, "lodeWebView() called.");
        if (NetWorkUtils.isNetworkConnected(this)) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.setClickable(true);

            mWebView.addJavascriptInterface(new WebJSPInterfaceMethedBean(),"papa");
            mWebView.loadUrl(url);
        } else {
            showLodingFailed();
        }
    }



    public class WebJSPInterfaceMethedBean {


        /**
         * 获取IMEI
         *
         * @param
         */
        @JavascriptInterface
        public String getIMEI() {
            return SystemInfoUtils.getInstance(MainActivity.this).getDeviceId();
        }

        /**
         * 获取获取手机型号
         *
         * @param
         */
        @JavascriptInterface
        public String getPhoneModle() {
            return SystemInfoUtils.getInstance(MainActivity.this).getMobileMODEL();
        }

        /**
         * 获取获取手机型号
         *
         * @param
         */
        @JavascriptInterface
        public String getVendor() {
            return SystemInfoUtils.getInstance(MainActivity.this).getVendor();
        }

        /**
         * 获取android 版本
         *
         * @param
         */
        @JavascriptInterface
        public String getAndroidVersion() {
            return SystemInfoUtils.getInstance(MainActivity.this).getMobileSystenmVersion();
        }

        /**
         * 查询app是否已安装
         */
        @JavascriptInterface
        public boolean checkAppHasInstall(String packageName) {
            boolean hasInstall = false;
            try {
                hasInstall = SystemInfoUtils.getInstance(MainActivity.this).checkInstall(MainActivity.this, packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hasInstall;
        }

        /**
         * 获取app版本号1.5.3
         *
         * @param
         */
        @JavascriptInterface
        public String getAppversionName() {
            return SystemInfoUtils.getInstance(MainActivity.this).getVersionName();
        }


        /**
         * 查询当前的版本号
         */
        @JavascriptInterface
        public int getNowVersion() {
            return SystemInfoUtils.getInstance(MainActivity.this).getVersionCode();
        }


        /**
         * 设置屏幕横屏竖屏 0 默认  1横屏 2 竖屏
         *
         * @param
         */
        @JavascriptInterface
        public void setScreenLandOrPort(final int screenLandOrPort) {
            mHandler.post(new Runnable() {
                public void run() {
                    switch (screenLandOrPort) {

                        case 0:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            break;
                        case 1:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                        case 2:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;

                    }

                }
            });
        }

        @JavascriptInterface
        public String getAccountVer2() {
            String result="";

            result = PrefUtil.getInstance(MainActivity.this).getAccountVer2();
            if (null==result||"".equals(result)) {

                result = "{" +
                        "  \"hasLogin\": false" +
                        "}";
            }

            return result;

        }

        @JavascriptInterface
        public void setAccountVer2(String uid,String token) {
            String s="{" +
                    "  \"hasLogin\": true," +
                    "  \"accountBean\": {" +
                    "    \"uid\": "+uid+"," +
                    "    \"account\": \"11\"," +
                    "    \"nickname\": \"nick\"," +
                    "    \"papaMoney\": 0," +
                    "    \"registerTime\": 0," +
                    "    \"avatarSrc\": \"ss\"," +
                    "    \"gender\": 0," +
                    "    \"token\": \""+token+"\"," +
                    "    \"status\": false" +
                    "  }" +
                    "}";

            PrefUtil.getInstance(MainActivity.this).setAccountVer2(s);

        }

    }

    private class WebViewClientXY extends WebViewClient {

        public WebViewClientXY(Context context) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url2) {
            Log.d(TAG, "shouldOverrideUrlLoading() called.");
            if (NetWorkUtils.isNetworkConnected(MainActivity.this)) {
                if (url2.startsWith("http") || url2.startsWith("https") || url2.startsWith("file://")) {
                    view.loadUrl(url2);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                    url = url2;
                } else if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        MainActivity.this.startActivity(intent);
                    } catch (Exception e) {

                    }

                    return true;
                } else {
                    try {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ToastUtils.getInstance(MainActivity.this).showToastSystem("网络连接异常，请检查网络连接");
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url2, Bitmap favicon) {
            super.onPageStarted(view, url2, favicon);
            isError = false;
            Log.d(TAG, "onPageStarted() called." + url2);
            showLoding();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished() called." + url);
            //显示默认的失败界面的url
            if (url.equals("data:text/html,chromewebdata") || url.equals("about:blank") || isError) {
                showLodingFailed();
                return;
            }
            afterLoading();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(TAG, "onReceivedError() called." + failingUrl);
            isError = true;
            view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            showLodingFailed();
        }
    }

    void afterLoading(){
        loding_layout.setVisibility(View.GONE);
        loding_faile.setVisibility(View.GONE);
    }

    void showLoding() {
        Log.d(TAG, "showLoding() called.");
        loding_layout.setVisibility(View.VISIBLE);
        loding_faile.setVisibility(View.GONE);
        if (mWebView != null)
            mWebView.setVisibility(View.VISIBLE);
    }

    void showLodingFailed() {
        Log.d(TAG, "showLodingFailed() called.");
        loding_faile.setVisibility(View.VISIBLE);
        loding_layout.setVisibility(View.GONE);
        if (mWebView != null)
            mWebView.setVisibility(View.GONE);
    }

    void openSetting(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        this.startActivity(intent);
    }
}
