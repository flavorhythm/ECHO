package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.ValueChangeSupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import data.DataAccessObject;

/**
 * Created by zyuki on 6/2/2016.
 */
public abstract class BaseFragment extends Fragment implements PropertyChangeListener {
    public static BaseFragment thisFragment;

    protected static Callback callback;
    protected static DataAccessObject dataAccess;
    protected static ValueChangeSupport valueChange;

    protected String fragName;

    protected boolean hasModelChangeListener = false;

    public String getFragName() {
        return fragName;
    }

    public void setFragName(String fragName) {
        this.fragName = fragName;
    }
//
//    public static BaseFragment newInstance() {
//        Log.v("FragmentDocuments", "notifyAndSetModelNameChange: ModelName has changed");
//        if(thisFragment == null) thisFragment = new BaseFragment();
//        return thisFragment;
//    }

    @Override
    public void onAttach(Context context) {
        Log.v("BaseFragment", "onAttach");
        super.onAttach(context);
        callback = (Callback)context;
    }

    @Override
    public void onDetach() {
        Log.v("BaseFragment", "onDetach");
        super.onDetach();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("BaseFragment", "onCreate");
        super.onCreate(savedInstanceState);

        dataAccess = ((DataAccessApplication)getActivity().getApplication()).getDataAccessObject();
        valueChange = ((DataAccessApplication)getActivity().getApplication()).getValueChangeSupport();
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.v("BaseFragment", "onViewCreated: " + fragmentView.toString());
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.v("BaseFragment", "onResume: " + getContext().toString());
        Log.v("BaseFragment", "onResume: is equals? " + String.valueOf(callback.equals(getContext())));
        super.onResume();

        if(callback != null && !callback.equals(getContext())) callback = (Callback)getContext();
        Log.v("BaseFragment", "onResume: is now? " + String.valueOf(callback.equals(getContext())));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Log.d("BaseFragment", "propertyChange: " + event.getPropertyName());
        String propName = event.getPropertyName();
        if(propName != null) {
            if(propName.equals(ValueChangeSupport.PROPERTY_MODEL)) {
                //TODO: run static "update" methods for the fragment that user is on
                Log.d("BaseFragment", "propertyChange: current fragment updated");
                updateFragContent(event.getNewValue().toString());
            }
        }
    }

    protected abstract void updateFragContent(String modelName);

    public interface Callback {
        View.OnClickListener getCardListnener();
        void setToolbar(Toolbar toolbar);
        void closeDrawer(int gravity);
    }
}
