package fragment.static_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import fragment.FragmentBase;

/**
 * Created by zyuki on 6/27/2016.
 */
public class FragmentToolbar extends FragmentBase {
    private static FragmentToolbar thisFragment;

    private static Toolbar toolbar;

    public static FragmentToolbar newInstance() {
        if(thisFragment == null) thisFragment = new FragmentToolbar();
        return thisFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        toolbar = (Toolbar)view;

        callback.setToolbar(toolbar);
    }

    public static void setGarageBtnVisibility(boolean visibility) {
        //TODO: make fade
        callback.setGarageBtnVisibility(visibility);
    }
}
