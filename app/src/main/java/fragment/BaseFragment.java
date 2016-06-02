package fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by zyuki on 6/2/2016.
 */
public class BaseFragment extends Fragment {
    protected Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (Callback)context;
    }

    public interface Callback {
        String getFragName();
    }
}
