package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;

/**
 * Created by zyuki on 6/7/2016.
 */
public class FragmentCardDisplay extends BaseFragment {
    public static final String CARD_CONTENT_KEY = "content_pos";

    private int position;

    public static FragmentCardDisplay newInstance() {
        return new FragmentCardDisplay();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_documents;
        View fragView = inflater.inflate(layoutRes, container, false);
        ((TextView)fragView.findViewById(R.id.test)).setText(String.valueOf(position));

        return fragView;
    }

    public void posOfCard(int position) {
        this.position = position;
    }
}
