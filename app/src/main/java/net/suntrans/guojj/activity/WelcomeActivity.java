package net.suntrans.guojj.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import net.suntrans.guojj.App;
import net.suntrans.guojj.MainActivity;
import net.suntrans.guojj.R;
import net.suntrans.guojj.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;

/**
 * Created by Looney on 2017/7/21.
 */

public class WelcomeActivity extends BasedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            check();
        }
    }

    private boolean ischeck = false;

    private void check() {
        if (ischeck) {
            return;
        }
        ischeck = true;
        try {
            String access_token = App.getSharedPreferences().getString("access_token", "-1");
            String expires_in = App.getSharedPreferences().getString("expires_in", "-1");
            String user_id = App.getSharedPreferences().getString("user_id", "-1");

            long firsttime = App.getSharedPreferences().getLong("firsttime", -1l);
            long currenttime = System.currentTimeMillis();
            long d = (currenttime - firsttime) / 1000;

            if (access_token.equals("-1") || expires_in.equals("-1") || firsttime == -1l ) {
                handler.sendEmptyMessageDelayed(START_LOGIN, 400);
            } else {
                if (d > (Long.valueOf(expires_in) - 1 * 24 * 3600)) {
                    handler.sendEmptyMessageDelayed(START_LOGIN, 400);
                } else {
                    handler.sendEmptyMessageDelayed(START_MAIN, 400);
                }
            }
        } catch (Exception e) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 400);
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    final int START_LOGIN = 0;
    final int START_MAIN = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_LOGIN) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
            if (msg.what == START_MAIN) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }
    };


    public void checkPermission() {
        final List<PermissonItem> permissionItems = new ArrayList<PermissonItem>();
        permissionItems.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限", R.drawable.permission_ic_memory));
        permissionItems.add(new PermissonItem(Manifest.permission.ACCESS_NETWORK_STATE, "获取您的网络状态", R.drawable.ic_network));
        HiPermission.create(WelcomeActivity.this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        LogUtil.i("关闭");
                    }

                    @Override
                    public void onFinish() {
//                        LogUtil.i("结束了我的儿子");
                        check();
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                        LogUtil.i("拒绝了权限" + permisson);
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        LogUtil.i("允许：" + permisson);
                    }
                });
    }
}
