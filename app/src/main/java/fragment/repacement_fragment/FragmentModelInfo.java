package fragment.repacement_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.R;
import com.echo_usa.echo.ValueChangeSupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import adapter.ModelInfoAdapter;
import data.Model;
import fragment.FragmentBase;
import util.MetricCalcs;
import widget.EmptyModelView;
import widget.ModelDrawerView;
import widget.ModelInfoList;

/**
 * Created by zyuki on 7/15/2016.
 */
public class FragmentModelInfo extends FragmentBase
        implements PropertyChangeListener, View.OnClickListener, ModelDrawerView.Callback {

    private static FragmentModelInfo thisFragment;
    private static boolean hasModelChangeListener = false;

    private static ValueChangeSupport valueChange;
    private Animation viewUpAnim, viewDownAnim;

    private CardTypeCheck cardTypeCheck;

    //private RecyclerView cardRecycler;
    private ModelInfoList listView;

    private TextSwitcher modelInfoTitleSwitcher;

    //private Button manualBtn, maintenanceBtn, specsBtn;
    private EmptyModelView emptyModelView;

    private ModelDrawerView modelDrawerView;

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
        cardTypeCheck = new CardTypeCheck();

        viewUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_up);
        viewUpAnim.setInterpolator(new FastOutSlowInInterpolator());

        viewDownAnim = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_down);
        viewDownAnim.setInterpolator(new FastOutSlowInInterpolator());

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

        modelDrawerView = (ModelDrawerView)fragView.findViewById(R.id.modelInfo_view);
        emptyModelView = (EmptyModelView)fragView.findViewById(R.id.modelInfo_empty);

//        manualBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_documentation);
//        maintenanceBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_maintenance);
//        specsBtn = (Button)fragView.findViewById(R.id.modelInfo_btn_specs);

        listView = (ModelInfoList)fragView.findViewById(R.id.modelInfo_docs_list);


        //cardRecycler = (RecyclerView)fragView.findViewById(R.id.modelInfo_recycler_cards);
        modelInfoTitleSwitcher = (TextSwitcher)fragView.findViewById(R.id.modelInfo_text_titleText);

        setupTextSwitcher();

        //cardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ModelInfoAdapter adapter = new ModelInfoAdapter(getContext(), R.layout.item_docs_list, dataAccess.getFiles(), callback.getCardListnener());
        listView.setAdapter(adapter);

//        cardRecycler.setAdapter(new ModelInfoAdapter(callback.getCardListnener()));
//        cardRecycler.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                thisFragment.modelInfoView.toggleDrawer(false);
//                thisFragment.cardTypeCheck.setDisplayedCardType();
//            }
//        });

        modelDrawerView.setCallback(thisFragment);

//        Button button = (Button)fragView.findViewById(R.id.test_toggle_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                modelInfoView.toggleDrawer(false);
//            }
//        });

        return fragView;
    }

    @Override
    public void passAnimationValue(int inverseValue) {
        listView.receiveAnimationValue(inverseValue);
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentDocuments", "onViewCreated");
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateContents(valueChange.getDisplayedModel());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Log.d("FragmentBase", "propertyChange: " + event.getPropertyName());

        String propName = event.getPropertyName();
        if(propName != null) {
            if(propName.equals(ValueChangeSupport.PROPERTY_MODEL)) {
                Model model = (Model)event.getNewValue();
                //thisFragment.cardTypeCheck.raiseSkipFlag();

                updateContents(model);

//                if(!thisModel.isEmpty()) {
//                    thisFragment.modelInfoTitleSwitcher.setText(thisModel.getModelName());
//                    thisFragment.modelInfoView.updateModelImage(thisModel.getImgResource());
//
//                    if(thisFragment.emptyModelView.isVisible())
//                        thisFragment.emptyModelView.startAnimation(EmptyModelView.HIDE_ANIM);
//                }
            }
        }
    }

    private void updateContents(Model model) {
        if(!model.isEmpty()) {
            thisFragment.modelInfoTitleSwitcher.setText(model.getModelName());
            thisFragment.modelDrawerView.updateModelImage(model.getImgResource());

            if(thisFragment.emptyModelView.isVisible())
                thisFragment.emptyModelView.startAnimation(EmptyModelView.HIDE_ANIM);
        }
    }

    @Override
    public void onClick(View v) {
        int cardType = -1;

//        switch(v.getId()) {
//            case R.id.modelInfo_btn_documentation: cardType = Card.CARD_TYPE_DOC; break;
//            case R.id.modelInfo_btn_maintenance: cardType = Card.CARD_TYPE_MAINT; break;
//            case R.id.modelInfo_btn_specs: cardType = Card.CARD_TYPE_SPECS; break;
//        }

//        nullifyListeners();
//        thisFragment.cardTypeCheck.setEnqueued(cardType);
        thisFragment.modelDrawerView.toggleDrawer(cardTypeCheck.isUpdateRequired());
    }

    private void setupTextSwitcher() {
        modelInfoTitleSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView modelInfoTitle = new TextView(getContext());
                modelInfoTitle.setTextSize(18);

                int padding = MetricCalcs.dpToPixels(10);
                modelInfoTitle.setPadding(padding, padding, padding, padding);

                modelInfoTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                modelInfoTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                modelInfoTitle.setAllCaps(true);

                return modelInfoTitle;
            }
        });

        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_out);

        modelInfoTitleSwitcher.setInAnimation(in);
        modelInfoTitleSwitcher.setOutAnimation(out);
    }

    private class CardTypeCheck {
        static final int EMPTY = -1;
        int enqueuedCardType = EMPTY;
        int displayedCardType = EMPTY;

        boolean skipFlag = false;

        void setEnqueued(int cardType) {this.enqueuedCardType = cardType;}
        int getEnqueued() {return enqueuedCardType;}

        void raiseSkipFlag() {skipFlag = true;}
        void dropSkipFlag() {skipFlag = false;}

        boolean isUpdateRequired() {
            boolean isDisplayedFilled = displayedCardType == EMPTY;
            boolean isEnqueuedDisplayed = displayedCardType == enqueuedCardType;

            return !skipFlag && (isDisplayedFilled || !isEnqueuedDisplayed);
        }

        void setDisplayedCardType() {
            this.displayedCardType = this.enqueuedCardType;
            setEnqueued(EMPTY);
        }
    }
}
