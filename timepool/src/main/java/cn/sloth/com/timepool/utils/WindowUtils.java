package cn.sloth.com.timepool.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author Nigalinson
 * @date 2017/6/26
 */
public class WindowUtils {

    public static int windowX(Activity appContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        appContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

}
