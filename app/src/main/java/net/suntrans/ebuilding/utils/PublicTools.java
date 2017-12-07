package net.suntrans.ebuilding.utils;

import android.os.Build;
import android.view.View;
import android.content.Context;

import net.suntrans.ebuilding.R;

/**
 * Created by Looney on 2017/12/7.
 * Des:
 */

public class PublicTools {
    public static void paddingTopRootView(Context paramContext, View paramView)
    {
        if (Build.VERSION.SDK_INT >= 19) {
            paramView.setPadding(0, StatusBarCompat.getStatusBarHeight(paramContext), 0, 0);
        }
    }
}
