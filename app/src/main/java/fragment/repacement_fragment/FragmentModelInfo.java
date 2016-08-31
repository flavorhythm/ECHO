package fragment.repacement_fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.R;
import com.echo_usa.echo.ValueChangeSupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import adapter.ModelInfoAdapter;
import data.Card;
import data.DataAccessObject;
import fragment.FragmentBase;
import fragment.FragmentRouter;
import util.FragName;
import util.MetricCalcs;
import widget.ChipView;
import widget.ModelInfoView;

/**
 * Created by zyuki on 7/15/2016.
 */
public class FragmentModelInfo extends FragmentBase
        implements PropertyChangeListener, View.OnClickListener, ModelInfoView.Callback {

    private static FragmentModelInfo thisFragment;
    private static boolean hasModelChangeListener = false;

    private static ValueChangeSupport valueChange;
    private Animation noModelHideAnim;

    private Animation modelImageAnim;
    private View noModelView;
    private RelativeLayout recyclerViewGroup;
    private RecyclerView cardRecycler;
    private ImageView upperImage, lowerImage;
//    private TextView modelInfoTitleSwitcher;
    private ChipView modelType, modelName, modelSerial;

    private TextSwitcher modelInfoTitleSwitcher;

    private Button manualBtn, maintenanceBtn, specsBtn;

    private ModelInfoView modelInfoView;

    //private ModelInfoAdapter adapter;

//    private static int listenerCounter = 0;

//    public void updateFragContent(String modelName) {
//        if(FragmentRouter.isThisFragDisplayed(FragName.DOCS)) {
//            updateCardData(modelName);
//
//
//            if(FragmentRouter.isInstantiated()) FragmentRouter.hideNoModelView();
//        }
//    }

    public static FragmentModelInfo newInstance() {
        if(thisFragment == null) thisFragment = new FragmentModelInfo();
        return thisFragment;
    }

    protected ValueChangeSupport getValueChange() {return valueChange;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onCreate");
        super.onCreate(savedInstanceState);

        valueChange = ((DataAccessApplication)getActivity().getApplication()).getValueChangeSupport();

//        final Animation modelInfoAnimOpen = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_up);
//
//        valueChange = ((DataAccessApplication)getActivity().getApplication()).getValueChangeSupport();
//        modelImageAnim = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_down);
//        modelImageAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                recyclerViewGroup.startAnimation(modelInfoAnimOpen);
//                updateContentModelSwitch(valueChange.getSelectedModel());
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
////
        if(!hasModelChangeListener) {
            valueChange.addPropertyChangeListener(this);
            hasModelChangeListener = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        int layoutRes = R.layout.frag_model_info;
        View fragView = inflater.inflate(layoutRes, container, false);

        modelInfoView = (ModelInfoView)fragView.findViewById(R.id.modelInfo_view);
        manualBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_documentation);
        maintenanceBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_maintenance);
        specsBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_specs);

//        noModelView = fragView.findViewById(R.id.no_model_view);
//        cardRecycler = (RecyclerView)fragView.findViewById(R.id.modelInfo_recycler_cards);
//        upperImage = (ImageView)fragView.findViewById(R.id.modelInfo_image_upper);
//        lowerImage = (ImageView)fragView.findViewById(R.id.modelInfo_image_lower);
//        modelInfoTitleSwitcher = (TextSwitcher)fragView.findViewById(R.id.modelInfo_text_titleText);
//        recyclerViewGroup = (RelativeLayout)fragView.findViewById(R.id.modelInfo_group_lower_image);
//
//        setupTextSwitcher();
//
//        cardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//
//        cardRecycler.setAdapter(new ModelInfoAdapter(callback.getCardListnener()));
//
//        Bitmap[] bitmapArray = splitImage();
//
//        upperImage.setImageBitmap(bitmapArray[0]);
//        lowerImage.setImageBitmap(bitmapArray[1]);
        //cardRecycler.setVisibility(View.INVISIBLE);

//        upperImage.getLayoutParams().height = MetricCalcs.getHeightForRatio(4,3);
//        upperImage.requestLayout();

//        fragView.findViewById(R.id.modelInfo_group_image).getLayoutParams().height = MetricCalcs.getHeightForRatio(4,3);
//        fragView.findViewById(R.id.modelInfo_group_image).requestLayout();

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onViewCreated");
        super.onViewCreated(fragmentView, savedInstanceState);

        manualBtn.setOnClickListener(thisFragment);
        maintenanceBtn.setOnClickListener(thisFragment);
        specsBtn.setOnClickListener(thisFragment);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Log.d("FragmentBase", "propertyChange: " + event.getPropertyName());
        String propName = event.getPropertyName();
        if(propName != null) {
            if(propName.equals(ValueChangeSupport.PROPERTY_MODEL)) {
                Log.d("FragmentBase", "propertyChange: current fragment updated");
                thisFragment.updateContentModelSwitch(event.getNewValue().toString());
//                if(noModelViewRequired) {
//                    thisFragment.removeNoModelView();
//                    noModelViewRequired = false;
//                }

                //TODO: only make viewable after data load. to be done after data in ECHO servers
                //if(FragmentRouter.isInstantiated()) FragmentRouter.hideNoModelView();
            }
        }
    }

    @Override
    public void onClick(View v) {
//        FragName selectedFrag = null;
        int cardType = -1;

        switch(v.getId()) {
            case R.id.modelInfo_btn_documentation: cardType = Card.CARD_TYPE_DOC; break;
            case R.id.modelInfo_btn_maintenance: cardType = Card.CARD_TYPE_MAINT; break;
            case R.id.modelInfo_btn_specs: cardType = Card.CARD_TYPE_SPECS; break;
        }

        if(cardType != -1) {
            modelInfoView.onMenuItemChanged(cardType, dataAccess.getCards(cardType));
        }
    }

    //
//    private void removeNoModelView() {
//        if((noModelView != null) && (noModelView.getVisibility() != View.GONE)) {
//            Log.d("FragmentBase", "updateFragContent: noModelView removed");
//
//            noModelView.startAnimation(noModelHideAnim);
//        }
//    }

//    @Override
//    public void onAnimationStart(Animation animation) {}
//
//    @Override
//    public void onAnimationEnd(Animation animation) {
//
//    }
//
//    @Override
//    public void onAnimationRepeat(Animation animation) {}

//    private Bitmap[] splitImage() {
//        Bitmap[] bitmapArray = new Bitmap[2];
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
//        int splitYPos = bitmap.getHeight() / 2;
//
//        bitmapArray[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos);
//        bitmapArray[1] = Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos);
//
//        return bitmapArray;
//    }

    public void updateContentFragSwitch() {
        //recyclerViewGroup.startAnimation(modelImageAnim);

    }

    public void updateContentModelSwitch(String modelName) {
        //TODO: update content here (on different model selected)
        //TODO: maybe check FragName to see whether is correct fragname
//        modelInfoTitleSwitcher.setText(modelName);
        modelInfoView.setModelName(modelName);

//        modelInfoView.setModelName(modelName);
//
//        List<Card> cardList = dataAccess.getCards(cardTypeForFrag(getFragName()));
//
//        if(modelName != null) {
//            //TODO: closes drawer and updates images in ModelInfoView
////            modelInfoView.updateRecycler(modelName, cardList);
////            ((ModelInfoAdapter) cardRecycler.getAdapter()).updateCardData(modelName, cardList);
//            //TODO: update model cardImage here
//        }

//        removeNoModelView();
    }

    private FragName getFragName() {
        FragName fragName = FragmentRouter.getEnqueuedFragName();
        if(fragName == null || fragName.equals(FragName.BLANK))
            fragName = FragmentRouter.getDisplayedFragName();

        return fragName;
    }

    @Override
    public ValueChangeSupport getValueChangeSupport() {return valueChange;}

    @Override
    public View.OnClickListener getListener() {return callback.getCardListnener();}

    //    private void setupTextSwitcher() {
//        modelInfoTitleSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                TextView modelInfoTitle = new TextView(getContext());
//                modelInfoTitle.setTextSize(14);
//
//                int padding = MetricCalcs.dpToPixels(10);
//                modelInfoTitle.setPadding(padding, padding, padding, padding);
//
//                modelInfoTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                modelInfoTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
//                modelInfoTitle.setAllCaps(true);
//
//                return modelInfoTitle;
//            }
//        });
//
//        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_in);
//        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_out);
//
//        modelInfoTitleSwitcher.setInAnimation(in);
//        modelInfoTitleSwitcher.setOutAnimation(out);
//    }

    private int cardTypeForFrag(FragName displayedFragName) {
        switch(displayedFragName) {
            case DOCS: return Card.CARD_TYPE_DOC;
            case SPECS: return Card.CARD_TYPE_SPECS;
            case MAINT: return Card.CARD_TYPE_MAINT;
            case BLANK: return Card.CARD_TYPE_PLACEHOLDER;
            default: return DataAccessObject.ERROR;
        }
    }
}
