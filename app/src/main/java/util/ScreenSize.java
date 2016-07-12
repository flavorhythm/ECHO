package util;

import android.app.Activity;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/23/2016.
 */
public class ScreenSize {
    public static int getHomeHeaderHeight(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int widthDp = metrics.widthPixels;

        return (widthDp / 16) * 9;
    }
    public static int getDrawerHeaderHeight(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int widthDp = metrics.widthPixels - getActionBarSize(activity);

        return (widthDp / 16) * 9;
    }

    public static int getActionBarSize(Activity activity) {
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);

        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return actionBarSize;
    }
}
