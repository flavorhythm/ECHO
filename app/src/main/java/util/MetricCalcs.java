package util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/23/2016.
 */
public class MetricCalcs {
    public static final int WIDTH_RATIO_16 = 16;
    public static final int HEIGHT_RATIO_9 = 9;

    private static int statusHeightInPx = 0;

    public static int dpToPixels(int targetDp) {
//        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
//        int widthPx = metrics.widthPixels;
//        float density = metrics.xdpi;
//
//        Log.v("MetricCalcs", "old: " + (int)(targetDp * (widthPx / density)));

        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
//        Log.v("MetricCalcs", "new: " + (int)(targetDp * scaledDensity));
        return (int)(targetDp * scaledDensity);

//        return (int)(targetDp * (widthPx / density));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void setStatusBarHeight(int height) {statusHeightInPx = height;}
    public static int getStatusBarHeight() {return statusHeightInPx;}

    public static int getHeightForRatio(int widthOfRatio, int heightOfRatio) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int widthPx = metrics.widthPixels;

        return (widthPx / widthOfRatio) * heightOfRatio;
    }

    public static int getDrawerHeaderHeight(int actionBarSize) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int widthPx = metrics.widthPixels - actionBarSize;

        return (widthPx / 16) * 9;
    }

    public static int getActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);

        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return actionBarSize;
    }
}
