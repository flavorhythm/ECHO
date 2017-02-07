package fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.echo_usa.echo.R;

import adapter.FragListAdapter;
import data.frag_list.Contact;
import util.FragSpec;

/**
 * Created by zyuki on 6/2/2016.
 */
public class FragmentContact extends FragmentBase {
    public static FragmentContact newInstance() {return new FragmentContact();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_contact;
        View customView = inflater.inflate(layoutRes, container, false);

        ListView list = (ListView)customView.findViewById(R.id.contact_list);
        FragListAdapter adapter = new FragListAdapter(
                getContext(), Contact.LAYOUT_COUNT, getDataAccess().getFragList(FragSpec.CONTACT)
        );
        list.setAdapter(adapter);

        return customView;
    }

    //TODO: check with Profile GPU Rendering
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
//
//        if(Build.VERSION.SDK_INT >= 11) {
//            if(animation == null && nextAnim != 0) {
//                animation = AnimationUtils.loadAnimation(getContext(), nextAnim);
//            }
//
//            if(animation != null) {
//                if(getView() != null) getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        if(getView() != null) getView().setLayerType(View.LAYER_TYPE_NONE, null);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            }
//        }
//
//        return animation;
//    }
}
