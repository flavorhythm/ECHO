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
public class FragmentContact extends BaseFragment {
    public static FragmentContact thisFragment;

    public static FragmentContact newInstance() {
        if(thisFragment == null) thisFragment = new FragmentContact();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "contact";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_contact;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }
}
