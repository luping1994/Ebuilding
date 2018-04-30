package net.suntrans.ebuilding.api;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.activity.AlertActivity;
import net.suntrans.ebuilding.activity.BasedActivity;
import net.suntrans.ebuilding.activity.LoginActivity;
import net.suntrans.ebuilding.rx.RxBus;
import net.suntrans.ebuilding.utils.ActivityUtils;
import net.suntrans.ebuilding.utils.UiUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiErrorHelper {
    public static void handleCommonError(final Context context, Throwable e) {
        if (e instanceof HttpException) {
            UiUtils.showToast("服务暂不可用");
        } else if (e instanceof IOException) {
            UiUtils.showToast("连接服务器失败");
        } else if (e instanceof ApiException) {
            int code = ((ApiException) e).code;
            if (code == ApiErrorCode.UNAUTHORIZED) {
//                showAlertDialog((BasedActivity) context);
                UiUtils.showToast("您的身份已过期,请重新登录");
                Intent intent =new Intent(context, AlertActivity.class);
                RxBus.getInstance().post(intent);
//                startAlertActivity(context);
//                ActivityUtils.showLoginOutDialogFragmentToActivity(((BasedActivity)context).getSupportFragmentManager(),"Alert");
//                AlertDialog.Builder builder = new AlertDialog.Builder(App.getApplication());
//                AlertDialog dialog = builder.setMessage("您的身份已过期,请重新登录")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ((BasedActivity)context).killAll();
//                            }
//                        }).create();
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
//                if (!dialog.isShowing()) {//此时提示框未显示
//                    dialog.show();
//                }
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.showToast("网络连接不可用");
            }else {
                UiUtils.showToast( ((ApiException) e).msg);
            }
        } else {
            UiUtils.showToast("unKnow error");
        }
    }

    private static  void startAlertActivity(Context context) {
        context.startActivity(new Intent(context, AlertActivity.class));
        ((BasedActivity)context).overridePendingTransition(0,0);
    }

    private static void showAlertDialog(final BasedActivity context){
        new AlertDialog.Builder(context)
                .setMessage("您的账号已从别处登录")
                .setCancelable(false)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("access_token","-1")
                                .putString("expires_in","-1")
                                .putLong("firsttime",-1l)
                                .commit();
                        context.killAll();
                    }
                })
                .setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("access_token","-1")
                                .putString("expires_in","-1")
                                .putLong("firsttime",-1l)
                                .commit();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context. startActivity(intent);
                        context. overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_fade_out);
                        context.killAll();
                    }
                })
                .create().show();
    }
}
