package net.suntrans.ebuilding.utils;

import android.view.View;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;

/**
 * Created by Looney on 2017/12/7.
 * Des:
 */

public class WebViewTool {


    private static void setZoomControlGone(View paramView) {
        Field localField = null;
        ZoomButtonsController localZoomButtonsController = null;

        try {
            localField = WebView.class.getDeclaredField("mZoomButtonsController");
            localField.setAccessible(true);
            localZoomButtonsController = new ZoomButtonsController(paramView);
            localZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            return;
        } catch (SecurityException e) {
            try {
                localField.set(paramView, localZoomButtonsController);
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
