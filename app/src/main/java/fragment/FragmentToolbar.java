package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/27/2016.
 */
public class FragmentToolbar extends BaseFragment {
    private static FragmentToolbar thisFragment;

    public static FragmentToolbar newInstance() {
        if(thisFragment == null) thisFragment = new FragmentToolbar();
        return thisFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "toolbar";
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_toolbar;
        View customView = inflater.inflate(layoutRes, container, false);


        return customView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback.setToolbar((Toolbar)view);
    }
}
