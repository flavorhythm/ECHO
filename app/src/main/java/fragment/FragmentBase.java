package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.echo_usa.echo.DataAccessApplication;

import data.DataAccessObject;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentBase extends Fragment {
    public static FragmentBase thisFragment;

    protected static Callback callback;
    protected static DataAccessObject dataAccess;
//    protected static ValueChangeSupport valueChange;
//
//    public static FragmentBase newInstance() {
//        Log.v("FragmentDocuments", "notifyAndSetModelNameChange: ModelName has changed");
//        if(thisFragment == null) thisFragment = new FragmentBase();
//        return thisFragment;
//    }

    @Override
    public void onAttach(Context context) {
        Log.v("FragmentBase", "onAttach");
        super.onAttach(context);

        callback = (Callback)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("FragmentBase", "onCreate");
        super.onCreate(savedInstanceState);

        dataAccess = ((DataAccessApplication)getActivity().getApplication()).getDataAccessObject();
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.v("FragmentBase", "onViewCreated: " + fragmentView.toString());
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.v("FragmentBase", "onResume: " + getContext().toString());
        Log.v("FragmentBase", "onResume: is equals? " + String.valueOf(callback.equals(getContext())));
        super.onResume();

        if(callback != null && !callback.equals(getContext())) callback = (Callback)getContext();
        Log.v("FragmentBase", "onResume: is now? " + String.valueOf(callback.equals(getContext())));
    }

    public interface Callback {
        View.OnClickListener getCardListnener();
        void scrollToolbar(int scrollY, int actionbarSize, int vertThreshold);
        void setToolbar(Toolbar toolbar);
        void closeDrawer(int gravity);
        void setGarageBtnVisibility(boolean visible);
    }
}
