package fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;

import util.MetricCalc;

/**
 * Created by zyuki on 6/2/2016.
 */
//TODO: change fragment to PreferenceFragment
    //http://viralpatel.net/blogs/android-preferences-activity-example/
    //https://developer.android.com/reference/android/preference/PreferenceFragment.html
public class FragmentSettings extends FragmentBase {
    public static FragmentSettings newInstance() {return new FragmentSettings();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
    }
}
