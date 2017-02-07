package util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.echo_usa.echo.R;

import java.util.Locale;

/**
 * Created by zyuki on 6/23/2016.
 */
public class MetricCalc {
    public static final int WIDTH_RATIO_16 = 16;
    public static final int HEIGHT_RATIO_9 = 9;

    private static int statusHeightInPx = 0;

    public static int dpToPxById(Context c, @DimenRes int dimenId) {
        return c.getResources().getDimensionPixelSize(dimenId);
    }
    /**Uses density constant to convert DP values to pixel.
     * SP values must use scaled density constant, something this method is not designed for**/
    public static int dpToPxByVal(int value) {
        float d = Resources.getSystem().getDisplayMetrics().density;
        return (int)(value * d + 0.5f);
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
        int widthPx = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (widthPx / widthOfRatio) * heightOfRatio;
    }

    public static int getDrawerHeaderHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (metrics.widthPixels / 16) * 9;
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
