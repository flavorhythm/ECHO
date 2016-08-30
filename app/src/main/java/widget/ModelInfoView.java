package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;
import com.echo_usa.echo.ValueChangeSupport;

import java.util.List;

import adapter.ModelInfoAdapter;
import data.Card;
import util.MetricCalcs;

/**
 * Created by ZYuki on 6/30/2016.
 */
public class ModelInfoView extends RelativeLayout implements Animation.AnimationListener {
    private Animation hideAnimation, showAnimation;
    private Animation noModelHideAnim;

    private Animation modelImageAnim;
    private View noModelView;
    private RelativeLayout recyclerViewGroup;
    private RecyclerView cardRecycler;
    private ImageView upperImage, lowerImage;
    private ChipView modelType, modelName, modelSerial;

    private TextSwitcher modelInfoTitleSwitcher;

    private ValueChangeSupport valueChange;

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//    }



    public ModelInfoView(Context context) {
        super(context);
        init();
    }

    public ModelInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModelInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModelInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        noModelHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_out);
        noModelHideAnim.setAnimationListener(ModelInfoView.this);

        valueChange = ((DataAccessApplication)getContext().getApplicationContext()).getValueChangeSupport();
        //View.OnClickListener listener = ((MainActivity)getContext()).getCardListnener();

//        hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_out);
//        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_in);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Log.v(this.toString(), (getRootView().getParent()).toString());

        inflater.inflate(R.layout.view_model_info, this, true);
//        Log.v("test","test");
        noModelView = findViewById(R.id.no_model_view);
        cardRecycler = (RecyclerView)findViewById(R.id.modelInfo_recycler_cards);
        upperImage = (ImageView)findViewById(R.id.modelInfo_image_upper);
        lowerImage = (ImageView)findViewById(R.id.modelInfo_image_lower);
        modelInfoTitleSwitcher = (TextSwitcher)findViewById(R.id.modelInfo_text_titleText);
        recyclerViewGroup = (RelativeLayout)findViewById(R.id.modelInfo_group_lower_image);
//
        setupTextSwitcher();
//
        cardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if(getContext() instanceof MainActivity)
            cardRecycler.setAdapter(new ModelInfoAdapter(((MainActivity)getContext()).getCardListnener()));
//
        Bitmap[] bitmapArray = splitImage();
//
        upperImage.setImageBitmap(bitmapArray[0]);
        lowerImage.setImageBitmap(bitmapArray[1]);

        if(!valueChange.getSelectedModel().equals(ValueChangeSupport.NO_SELECTION)) {
            noModelView.setVisibility(View.GONE);
            noModelView.setClickable(false);

            setModelName(valueChange.getSelectedModel());
        }
//        cardRecycler.setVisibility(View.INVISIBLE);
//
        //TODO: use this to remove nomodelview
//        noModelView.setVisibility(View.GONE);
//
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        setLayoutParams(params);

//        upperImage.getLayoutParams().height = MetricCalcs.getHeightForRatio(4,3);
//        upperImage.requestLayout();
//
//        findViewById(R.id.modelInfo_group_image).getLayoutParams().height = MetricCalcs.getHeightForRatio(4,3);
//        findViewById(R.id.modelInfo_group_image).requestLayout();


       // inflater.inflate(R.layout.view_model_info, (RelativeLayout)getRootView(), true);

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
//        if(!hasModelChangeListener) {
//            valueChange.addPropertyChangeListener(this);
//            hasModelChangeListener = true;
//        }
    }

    private void removeNoModelView() {
        if((noModelView != null) && (noModelView.getVisibility() != View.GONE)) {
            Log.d("FragmentBase", "updateFragContent: noModelView removed");

            noModelView.startAnimation(noModelHideAnim);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        noModelView.setVisibility(View.GONE);
        noModelView.setClickable(false);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
    }

    @Override
    protected void onAnimationEnd() {super.onAnimationEnd();}

//    public void startAnimation(boolean toShow) {
//        if(toShow) startAnimation(showAnimation);
//        else startAnimation(hideAnimation);
//    }

    public void setModelName(String modelName) {
        modelInfoTitleSwitcher.setText(modelName);
        removeNoModelView();
    }

    //TODO: need to build this
    public void setImage(){}

    public void updateRecycler(String modelName, List<Card> cardList) {
        ((ModelInfoAdapter)cardRecycler.getAdapter()).updateCardData(modelName, cardList);
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

//        modelInfoTitleSwitcher.setText("SRM-225");
    }

    private Bitmap[] splitImage() {
        Bitmap[] bitmapArray = new Bitmap[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        bitmapArray[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos);
        bitmapArray[1] = Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos);

        return bitmapArray;
    }
}
