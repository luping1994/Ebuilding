package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.R;

/**
 * Created by Looney on 2017/12/5.
 * Des:
 */

public class FloorPlanActivity extends BasedActivity {

    private String house_id;
    private String token;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentNavigation(true);
        setTranslucentStatus(true);
        setContentView(R.layout.activity_plan);
        house_id = getIntent().getStringExtra("house_id");
        token ="Bearer "+ App.getSharedPreferences().getString("access_token", "-1");
        webview = (WebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String js = "";
                js += "var newscript = document.createElement(\"script\");";
                js += "newscript.src=\"./js/designer.js\";";
                js += "newscript.onload=function(){"
                        + "init(\""
                        + token + "\",\""
                        + house_id + "\");};";
                js += "document.body.appendChild(newscript);";

//                System.out.println(js);
                webview.loadUrl("javascript:" + js);
            }
        });

        webview.setInitialScale(100);

        webview.loadUrl("file:///android_asset/plan/floor_plan.html");
        webview.addJavascriptInterface(new AndroidtoJs(), "control");
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void switchChannel(String control) {
            String[] split = control.split(",");
            String status = split[2].equals("true") ? "关闭" : "打开";
            new AlertDialog.Builder(FloorPlanActivity.this)
                    .setMessage("是否" + status + split[1])
                    .setPositiveButton("确定", null)
                    .setNegativeButton("取消", null).create().show();


        }
    }

    @Override
    protected void onDestroy() {
        webview.destroy();
        super.onDestroy();
    }
}
