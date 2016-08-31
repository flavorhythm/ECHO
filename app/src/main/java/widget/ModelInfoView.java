package widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
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
    private static final int LEFT = 100;
    private static final int RIGHT = 200;

    private Animation slideDownAnim, slideUpAnim;
    private Animation noModelHideAnim;

    private Animation modelImageAnim;
    private View noModelView;
    private RelativeLayout lowerImageGroup;
    private RecyclerView cardRecycler;
    private ImageSwitcher upperImageSwitcher, lowerImageSwitcher;

    private TextSwitcher modelInfoTitleSwitcher;

    private ValueChangeSupport valueChange = null;

    private List<Card> enqueuedCardList;

    private int enqueuedCardType = -1;

    private int displayedCardType = -1;

    private static Callback callback;

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
        //isInEditMode();

        View.OnClickListener cardListener = null;

        if(!isInEditMode()) {
            callback = (Callback)getContext();
            valueChange = callback.getValueChangeSupport();
            cardListener = callback.getListener();
        }

        noModelHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_out);
        noModelHideAnim.setAnimationListener(ModelInfoView.this);

//        slideImageUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_up);
//        slideImageDownAnim = AnimationUtils.loadAnimation(getContext(), R.anim.model_info_slide_down);
//
//        slideImageUpAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                updateRecycler(valueChange.getEnqueuedModelName(), enqueuedCardList);
//                lowerImageGroup.setTranslationY(MetricCalcs.dpToPixels(0));
//                lowerImageGroup.startAnimation(slideImageDownAnim);
//
//                enqueuedCardList = null;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        slideImageDownAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                lowerImageGroup.setTranslationY(MetricCalcs.dpToPixels(240));
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

//        if(valueChange == null && getContext().getApplicationContext() instanceof DataAccessApplication) {
//            valueChange = ((DataAccessApplication)getContext().getApplicationContext()).getValueChangeSupport();
//        }
        //View.OnClickListener listener = ((MainActivity)getContext()).getCardListnener();

//        hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_out);
//        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.no_model_fade_in);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Log.v(this.toString(), (getRootView().getParent()).toString());

        inflater.inflate(R.layout.view_model_info, this, true);
//        Log.v("test","test");
        noModelView = findViewById(R.id.no_model_view);
        cardRecycler = (RecyclerView)findViewById(R.id.modelInfo_recycler_cards);
        upperImageSwitcher = (ImageSwitcher)findViewById(R.id.modelInfo_image_upper);
        lowerImageSwitcher = (ImageSwitcher)findViewById(R.id.modelInfo_image_lower);
        modelInfoTitleSwitcher = (TextSwitcher)findViewById(R.id.modelInfo_text_titleText);
        lowerImageGroup = (RelativeLayout) findViewById(R.id.modelInfo_group_lower_image);
//
        setupTextSwitcher();
        setupImageSwitcher();
//
        cardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if(cardListener != null && getContext() instanceof MainActivity)
            cardRecycler.setAdapter(new ModelInfoAdapter(cardListener));

        moveImageLeftAnim();
//
//        Bitmap[] bitmapArray = splitImageBitmap();
////
//        upperImageSwitcher.setImageBitmap(bitmapArray[0]);
//        lowerImageSwitcher.setImageBitmap(bitmapArray[1]);

        if(!valueChange.getSelectedModel().equals(ValueChangeSupport.NO_SELECTION)) {
            noModelView.setVisibility(View.GONE);
            noModelView.setClickable(false);

            setModelName(valueChange.getSelectedModel());
        }

        upperImageSwitcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveImageLeftAnim();
            }
        });

//
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        setLayoutParams(params);

//        upperImageSwitcher.getLayoutParams().height = MetricCalcs.getHeightForRatio(4,3);
//        upperImageSwitcher.requestLayout();
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
        changeModelImage();
        removeNoModelView();
    }

    //TODO: need to build this
    public void setImage(){}

    public void onMenuItemChanged(int cardType, List<Card> cardList) {
        displayedCardType = cardType;



        if(enqueuedCardType == -1) {
            enqueuedCardType = cardType;
            enqueuedCardList = cardList;
            slideImageDownAnim();
        } else {
            enqueuedCardType = cardType;
            enqueuedCardList = cardList;
            slideImageUpAnim(true);
        }
        //TODO: start lower image animation
    }

    private void slideImageDownAnim() {
        lowerImageGroup
                .animate()
                .translationY(MetricCalcs.dpToPixels(210))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        updateRecycler(valueChange.getEnqueuedModelName(), enqueuedCardList);
                        enqueuedCardList = null; //TODO: necessary?

                        doSwitcherAnim(true);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.removeAllListeners();
                    }

                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                })
                .setStartDelay(100)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        cardRecycler
                .animate()
                .translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    private void slideImageUpAnim(boolean includeSlideDown) {
        if(includeSlideDown) {
            lowerImageGroup
                    .animate()
                    .translationY(MetricCalcs.dpToPixels(0))
                    .setListener(new Animator.AnimatorListener() {
                        @Override public void onAnimationStart(Animator animation) {
                            doSwitcherAnim(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            slideImageDownAnim();
                            animation.removeAllListeners();
                        }

                        @Override public void onAnimationCancel(Animator animation) {}
                        @Override public void onAnimationRepeat(Animator animation) {}
                    })
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .start();
        } else {
            lowerImageGroup
                    .animate()
                    .translationY(MetricCalcs.dpToPixels(0))
                    .setListener(new Animator.AnimatorListener() {
                        @Override public void onAnimationStart(Animator animation) {
                            doSwitcherAnim(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animation.removeAllListeners();
                        }

                        @Override public void onAnimationCancel(Animator animation) {}
                        @Override public void onAnimationRepeat(Animator animation) {}
                    })
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .start();
        }

        cardRecycler
                .animate()
                .translationY(150)
                .setStartDelay(50)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    private void doSwitcherAnim(boolean offset) {
        int offsetVal = 0;
        if(offset) offsetVal = 4;

        upperImageSwitcher.getCurrentView()
                .animate()
                .translationY(MetricCalcs.dpToPixels(offsetVal))
                .start();

        lowerImageSwitcher.getCurrentView()
                .animate()
                .translationY(MetricCalcs.dpToPixels(-1 * offsetVal))
                .start();
    }

    private void moveImageLeftAnim() {
        Drawable[] drawables = splitImageDrawable();

        upperImageSwitcher.setImageDrawable(drawables[0]);
        lowerImageSwitcher.setImageDrawable(drawables[1]);
    }

    private void moveImageRightAnim() {

    }

    private void changeModelImage() {
        enqueuedCardType = -1;
        slideImageUpAnim(false);
    }

    public void updateRecycler(String modelName, List<Card> cardList) {
        ((ModelInfoAdapter)cardRecycler.getAdapter()).updateCardData(modelName, cardList);
    }

    private void swipeModel(int direction) {
        //TODO: change models when button is clicked or image is swiped
        //TODO: will close model image tray
        //TODO: fires changeModelImage method
        switch(direction) {
            case LEFT: break;
            case RIGHT: break;
        }
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

    private void setupImageSwitcher() {
        final Bitmap[] bitmapArray = splitImageBitmap();

//        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmapArray[0]);

        upperImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });

        lowerImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });

        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.text_slide_out);

        upperImageSwitcher.setInAnimation(in);
        upperImageSwitcher.setOutAnimation(out);

        lowerImageSwitcher.setInAnimation(in);
        lowerImageSwitcher.setOutAnimation(out);

//        upperImageSwitcher.setImageBitmap(bitmapArray[0]);
//        lowerImageSwitcher.setImageBitmap(bitmapArray[1]);
    }

    private Drawable[] splitImageDrawable() {
        Drawable[] drawableArray = new Drawable[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        drawableArray[0] = new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos));
        drawableArray[1] = new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos));

        return drawableArray;
    }

    private Bitmap[] splitImageBitmap() {
        Bitmap[] bitmapArray = new Bitmap[2];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.srm_225);
        int splitYPos = bitmap.getHeight() / 2;

        bitmapArray[0] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), splitYPos);
        bitmapArray[1] = Bitmap.createBitmap(bitmap, 0, splitYPos, bitmap.getWidth(), splitYPos);

        return bitmapArray;
    }

    public interface Callback {
        ValueChangeSupport getValueChangeSupport();
        View.OnClickListener getListener();
    }
}
