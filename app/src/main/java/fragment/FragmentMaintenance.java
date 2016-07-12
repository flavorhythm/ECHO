package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import util.FragName;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentMaintenance extends BaseFragment {
    private static FragmentMaintenance thisFragment;

    public static FragmentMaintenance newInstance() {
        Log.v("FragmentDocuments", "notifyAndSetModelNameChange: ModelName has changed");
        if(thisFragment == null) thisFragment = new FragmentMaintenance();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {
        if(FragmentRouter.isThisFragDisplayed(FragName.MAINT)) {
            updateContent(modelName);

            //TODO: only make viewable after data load. to be done after data in ECHO servers
//            callback.hideNoModelView();
            if(FragmentRouter.isInstantiated()) FragmentRouter.hideNoModelView();
        }
    }

    private void updateContent(String modelName) {
        //TODO: update content here
        Log.v("FragmentDocuments", "updateContent: content updated");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("FragmentMaintenance", "onCreate");
        super.onCreate(savedInstanceState);

        this.fragName = "maintenance";
        if(!hasModelChangeListener) {
            Log.d("FragmentMaintenance", "onCreate: listener " + hasModelChangeListener + " at " + fragName);
            valueChange.addPropertyChangeListener(this);
            hasModelChangeListener = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentMaintenance", "onCreateView");
        int layoutRes = R.layout.frag_maintenance;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentMaintenance", "onViewCreated");
        super.onViewCreated(fragmentView, savedInstanceState);

        updateContent(valueChange.getSelectedModel());
    }
}
