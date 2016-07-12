package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;

import adapter.DocumentAdapter;
import data.DataAccessObject;
import util.FragName;
import widget.NoScrollRecyclerView;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentDocuments extends BaseFragment {
    private static FragmentDocuments thisFragment;

    private static final int SPAN_COUNT = 2;

    private TextView testText;

    public static FragmentDocuments newInstance() {
        Log.v("FragmentDocuments", "notifyAndSetModelNameChange: ModelName has changed");
        if(thisFragment == null) thisFragment = new FragmentDocuments();
        return thisFragment;
    }

    @Override
    public void updateFragContent(String modelName) {
        if(FragmentRouter.isThisFragDisplayed(FragName.DOCS)) {
            updateContent(modelName);

            //TODO: only make viewable after data load. to be done after data in ECHO servers
            //callback.hideNoModelView();
            if(FragmentRouter.isInstantiated()) FragmentRouter.hideNoModelView();
        }
    }

    private void updateContent(String modelName) {
        //TODO: update content here
        Log.v("FragmentDocuments", "updateContent: content updated");
        testText.setText(modelName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onCreate");
        super.onCreate(savedInstanceState);

        this.fragName = "documents";
        if(!hasModelChangeListener) {
            Log.d("FragmentDocuments", "onCreate: listener " + hasModelChangeListener + " at " + fragName);
            valueChange.addPropertyChangeListener(this);
            hasModelChangeListener = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        int layoutRes = R.layout.frag_documents;
        View fragView = inflater.inflate(layoutRes, container, false);
        testText = (TextView)fragView.findViewById(R.id.test_text);

        NoScrollRecyclerView recycler = (NoScrollRecyclerView)fragView.findViewById(R.id.document_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false));

        recycler.setAdapter(new DocumentAdapter(dataAccess.getCards(DataAccessObject.CARDS_FOR_DOCS)));

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onViewCreated");
        super.onViewCreated(fragmentView, savedInstanceState);

        updateContent(valueChange.getSelectedModel());
    }
}
