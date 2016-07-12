package fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import adapter.SpecsAdapter;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentSettings extends BaseFragment {
    private static FragmentSettings thisFragment;

    public static FragmentSettings newInstance() {
        if(thisFragment == null) thisFragment = new FragmentSettings();
        return thisFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "settings";
    }

    @Override
    public void updateFragContent(String modelName) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_settings;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());\

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);


    }
}
