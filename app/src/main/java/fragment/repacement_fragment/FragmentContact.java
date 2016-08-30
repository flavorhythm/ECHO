package fragment.repacement_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.R;

import adapter.ContactAdapter;
import fragment.FragmentBase;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentContact extends FragmentBase {
    public static FragmentContact thisFragment;

    public static FragmentContact newInstance() {
        if(thisFragment == null) thisFragment = new FragmentContact();
        return thisFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_contact;
        View customView = inflater.inflate(layoutRes, container, false);

        RecyclerView recycler = (RecyclerView)customView.findViewById(R.id.contactUs_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(new ContactAdapter(getContext(), R.layout.view_card_slim));

        return customView;
    }
}
