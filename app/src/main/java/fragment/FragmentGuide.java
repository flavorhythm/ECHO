package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentGuide extends BaseFragment {
    public static FragmentGuide newInstance() {
        Bundle args = new Bundle();

        FragmentGuide fragment = new FragmentGuide();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_guide;
        View fragView = inflater.inflate(layoutRes, container, false);
        ((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        return fragView;
    }
}
