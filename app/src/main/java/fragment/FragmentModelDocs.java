package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;

import adapter.FragListAdapter;
import data.frag_list.ModelDocs;
import data.navigation.Model;
import data.navigation.NavItem;
import util.FragSpec;
import widget.EchoLogoView;
import widget.ModelDrawerView;
import widget.ModelListView;

/**
 * Created by zyuki on 7/15/2016.
 */
public class FragmentModelDocs extends FragmentBase implements ModelDrawerView.Callback {
    private ModelListView listView;
    private EchoLogoView echoLogoView;
    private ModelDrawerView modelDrawerView;

    public static FragmentModelDocs newInstance(@Nullable Model m) {
        FragmentModelDocs f = new FragmentModelDocs();
        Bundle b = new Bundle();

        if(m != null) {
            b.putInt(NavItem.IMG_RES, m.getImgResource());
            b.putString(NavItem.CONTENT, m.getModelName());
            b.putString(NavItem.S_CONTENT, m.getSerialNum());
        }

        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i("FragmentModelDocs", "onCreateOptionsMenu: " + menu.getItem(0).toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        int layoutRes = R.layout.frag_model_info;
        View fragView = inflater.inflate(layoutRes, container, false);

        modelDrawerView = (ModelDrawerView)fragView.findViewById(R.id.modelInfo_view);
        echoLogoView = (EchoLogoView)fragView.findViewById(R.id.modelInfo_empty);

        listView = (ModelListView)fragView.findViewById(R.id.modelInfo_docs_list);

        FragListAdapter adapter = new FragListAdapter(
                getContext(), ModelDocs.LAYOUT_COUNT, getDataAccess().getFragList(FragSpec.MODEL_DOCS)
        );
        listView.setAdapter(adapter);

        modelDrawerView.setCallback(this);
        return fragView;
    }

    @Override
    public void passMotionEvent(MotionEvent event) {
        listView.onTouchEvent(event);
    }

    @Override
    public void passAnimationValue(int inverseValue) {
        listView.receiveAnimationValue(inverseValue);
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedBundle) {
        Log.d("FragmentDocuments", "onViewCreated");

        Bundle b = getArguments();
        if(b != null && !b.isEmpty()) {
            int res = b.getInt(NavItem.IMG_RES);
            String name = b.getString(NavItem.CONTENT);
            String serial = b.getString(NavItem.S_CONTENT);

            Model m = new Model(res, name, serial, 0);
            updateContents(m);
        }

        super.onViewCreated(fragmentView, savedBundle);
    }

    public void updateContents(Model model) {
        if(echoLogoView.isVisible()) echoLogoView.startAnimation(EchoLogoView.HIDE_ANIM);
        if(!modelDrawerView.updateModel(model)) {
            ((MainActivity)getActivity()).getSnackbar().show("Already displayed");
        }
    }
}
