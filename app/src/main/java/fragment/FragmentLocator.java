package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentLocator extends BaseFragment {
    public static FragmentLocator thisFragment;

    public static FragmentLocator newInstance() {
        if(thisFragment == null) thisFragment = new FragmentLocator();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "locator";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_locator;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }
}
