package widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import data.Card;
import fragment.FragmentRouter;
import util.FragName;
import util.MetricCalcs;

/**
 * Created by zyuki on 8/4/2016.
 */
public class EchoCard extends CardView {
    public static final int CARD_SIZE_SLIM = 0;
    public static final int CARD_SIZE_SMALL = 1;
    public static final int CARD_SIZE_LARGE = 2;

    private static final int NOT_IN_USE = -1;

    private static final int STD_ELEVATION = 1;
    private static final int STD_CORNER_RADIUS = 2;

    private static boolean isImageOnLeft = true;

    private int cardSize;
    private int listPos;

    private int largeImageRes;
    private int largeTitleRes;
    private int largeSubtitleRes;
    private int largeButtonRes;

    public EchoCard(Context context, int cardSize) {
        super(context);

        this.cardSize = cardSize;
        init(cardSize);
    }

    public EchoCard(Context context, int cardSize, boolean isImageOnLeft) {
        super(context);

        this.cardSize = cardSize;
        this.isImageOnLeft = isImageOnLeft;
        init(cardSize);
    }

    public int getCardSize() {return cardSize;}

    public void updateCard(int cardSize, Card card) {
        switch(cardSize) {
            case CARD_SIZE_SLIM:
                updateSlimCard();
                break;
            case CARD_SIZE_SMALL:
                updateSmallCard();
                break;
            case CARD_SIZE_LARGE:
                updateLargeCard();
                break;
        }
    }

    private void updateSlimCard() {

    }

    private void updateSmallCard() {

    }

    private void updateLargeCard() {

    }

    private void init(int cardSize) {
        int layoutRes = 0;

//        int radius = STD_CORNER_RADIUS;
        int width = 0;
        int height = LayoutParams.WRAP_CONTENT;
        boolean useCompatPadding = true;

        switch(cardSize) {
            case CARD_SIZE_SLIM:
                layoutRes = R.layout.view_card_slim;

                //radius = 0;
                width = LayoutParams.MATCH_PARENT;
                useCompatPadding = false;
                break;
            case CARD_SIZE_SMALL:
                layoutRes = R.layout.view_card_small;

                //radius = STD_CORNER_RADIUS;
                width = LayoutParams.WRAP_CONTENT;
                height = LayoutParams.MATCH_PARENT;
                useCompatPadding = true;
            break;
            case CARD_SIZE_LARGE:
                layoutRes = getAlternatingLayout();

                //radius = STD_CORNER_RADIUS;
                width = LayoutParams.MATCH_PARENT;
                useCompatPadding = true;
                break;
        }

        if(width != 0) {
            LayoutParams params = new LayoutParams(width, height);

            if(cardSize == Card.CARD_SIZE_SMALL) {
                int dpVal = 16;
                int pxVal = MetricCalcs.dpToPixels(dpVal);
                params.setMargins(pxVal / 2, pxVal, pxVal / 2, pxVal);
            }

            if(cardSize == Card.CARD_SIZE_LARGE) {
                int dpVal = 8;
                int pxVal = MetricCalcs.dpToPixels(dpVal);
                params.setMargins(pxVal, pxVal / 2, pxVal, pxVal / 2);
            }

            setLayoutParams(params);
        }

        if(layoutRes != 0) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layoutRes, this, true);
        }

        setPreventCornerOverlap(false);
        setUseCompatPadding(useCompatPadding);
        setCardElevation(MetricCalcs.dpToPixels(STD_ELEVATION));
        //        setElevation(MetricCalcs.dpToPixels(STD_ELEVATION));
        setRadius(MetricCalcs.dpToPixels(STD_CORNER_RADIUS));

//        if(cardSize == Card.CARD_SIZE_LARGE) {
//            getCardImageView(cardSize).getLayoutParams().height = MetricCalcs.getHeightForRatio(
//                    MetricCalcs.WIDTH_RATIO_16,
//                    MetricCalcs.HEIGHT_RATIO_9
//            );
//        }
    }

    private int getAlternatingLayout() {
        if(isImageOnLeft) {
            largeImageRes = R.id.card_med_image_left;
            largeTitleRes = R.id.card_med_title_left;
            largeSubtitleRes = R.id.card_med_subtitle_left;
            largeButtonRes = R.id.card_med_btn_left;
        } else {
            largeImageRes = R.id.card_med_image_right;
            largeTitleRes = R.id.card_med_title_right;
            largeSubtitleRes = R.id.card_med_subtitle_right;
            largeButtonRes = R.id.card_med_btn_right;
        }

        return isImageOnLeft ? R.layout.view_card_medium_left : R.layout.view_card_medium_right;
    }

    public View getImageWrapper(int cardSize) {
        switch(cardSize) {
            case Card.CARD_SIZE_SMALL: return findViewById(R.id.card_small_imageHolder);
            default: return null;
        }
    }

    public void adjustImageView() {
        int cardSize = Card.CARD_SIZE_SMALL;
        LayoutParams params = new LayoutParams(MetricCalcs.dpToPixels(40), ViewGroup.LayoutParams.MATCH_PARENT);
        getCardImageView(cardSize).setLayoutParams(params);
    }

    public ImageView getCardImageView(int cardSize) {
        switch(cardSize) {
            case Card.CARD_SIZE_SLIM: return (ImageView)findViewById(R.id.card_slim_image);
            case Card.CARD_SIZE_SMALL: return (ImageView)findViewById(R.id.card_small_image);
            case Card.CARD_SIZE_LARGE: return (ImageView)findViewById(largeImageRes);
            default: return null;
        }
    }

    public TextView getCardTitleView(int cardSize) {
        switch(cardSize) {
            case Card.CARD_SIZE_SLIM: return (TextView)findViewById(R.id.card_slim_title);
            case Card.CARD_SIZE_SMALL: return (TextView)findViewById(R.id.card_small_title);
            case Card.CARD_SIZE_LARGE: return (TextView)findViewById(largeTitleRes);
            default: return null;
        }
    }

    public TextView getCardSubtitleView(int cardSize) {
        switch(cardSize) {
            case Card.CARD_SIZE_SLIM: return (TextView)findViewById(R.id.card_slim_subtitle);
            case Card.CARD_SIZE_LARGE: return (TextView)findViewById(largeSubtitleRes);
            default: return null;
        }
    }

    public Button getCardBtnView(int cardSize) {
        switch(cardSize) {
           //case Card.CARD_SIZE_SMALL: return (Button)findViewById(R.id.card_small_btn);
            case Card.CARD_SIZE_LARGE: return (Button)findViewById(largeButtonRes);
            default: return null;
        }
    }
//    }
}
