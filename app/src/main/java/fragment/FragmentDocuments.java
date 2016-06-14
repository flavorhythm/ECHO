package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;

import adapter.DocumentAdapter;
import data.DataAccessObject;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentDocuments extends BaseFragment {
    private static final int SPAN_COUNT = 2;

    public static FragmentDocuments newInstance() {
        Bundle args = new Bundle();

        FragmentDocuments fragment = new FragmentDocuments();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_documents;
        View fragView = inflater.inflate(layoutRes, container, false);

        RecyclerView recycler = (RecyclerView)fragView.findViewById(R.id.document_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false));

        recycler.setAdapter(new DocumentAdapter(callback.getCards(DataAccessObject.CARDS_FOR_DOCS)));

        return fragView;
    }
}
