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
 * Created by zyuki on 6/7/2016.
 */
public class FragmentCardDisplay extends BaseFragment {
    private static FragmentCardDisplay thisFragment;

    public static final String CARD_CONTENT_KEY = "content_pos";

    private int position;

    public static FragmentCardDisplay newInstance() {
        if(thisFragment == null) thisFragment = new FragmentCardDisplay();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragName = "card_display";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_card_display;
        View fragView = inflater.inflate(layoutRes, container, false);

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    public void posOfCard(int position) {
        this.position = position;
    }
}
