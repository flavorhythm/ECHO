package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import adapter.SpecsAdapter;
import util.FragName;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentSpecifications extends BaseFragment {
    private TextView modelName;
    private ImageView modelImage;
    private ListView specsList;

    private static FragmentSpecifications thisFragment;

    public static FragmentSpecifications newInstance() {
        Log.v("FragmentDocuments", "notifyAndSetModelNameChange: ModelName has changed");
        if(thisFragment == null) thisFragment = new FragmentSpecifications();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {
        if(FragmentRouter.isThisFragDisplayed(FragName.SPECS)) {
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
        Log.d("FragmentSpecifications", "onCreateView");
        int layoutRes = R.layout.frag_specs;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        modelName = (TextView)fragView.findViewById(R.id.specs_text_modelText);
        modelImage = (ImageView)fragView.findViewById(R.id.specs_image_modelImage);
        specsList = (ListView)fragView.findViewById(R.id.specs_list_specsList);

        specsList.setAdapter(new SpecsAdapter(getActivity(), R.layout.item_specs_list, dataAccess.getSpecsList()));

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentSpecifications", "onViewCreated");
        super.onViewCreated(fragmentView, savedInstanceState);

        updateContent(valueChange.getSelectedModel());
    }
}
