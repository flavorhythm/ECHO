package adapter.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.R;

import data.Card;
import widget.EchoCard;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class CardHolder extends BaseHolder {
    public int imageRes;

    public CardView thisCard;
    public View cardImgWrapper;
    public ImageView cardImage;
    public TextView cardTitle, cardSubtitle;
    public Button cardBtn;

    public CardHolder(View card) {
        super(card);

        EchoCard echoCard = (EchoCard)card;
        int cardSize = echoCard.getCardSize();

        switch(cardSize) {
            case Card.CARD_SIZE_SLIM:
                cardImage = echoCard.getCardImageView(cardSize);
                cardTitle = echoCard.getCardTitleView(cardSize);
                cardSubtitle = echoCard.getCardSubtitleView(cardSize);
                thisCard = (CardView)card;
                break;
            case Card.CARD_SIZE_SMALL:
                cardImgWrapper = echoCard.findViewById(R.id.card_small_imageHolder);

                cardImage = echoCard.getCardImageView(cardSize);
                cardTitle = echoCard.getCardTitleView(cardSize);
                //cardBtn = echoCard.getCardBtnView(cardSize);
                thisCard = (CardView)card;
                break;
            case Card.CARD_SIZE_LARGE:
                cardImage = echoCard.getCardImageView(cardSize);
                cardTitle = echoCard.getCardTitleView(cardSize);
                cardSubtitle = echoCard.getCardSubtitleView(cardSize);
                cardBtn = echoCard.getCardBtnView(cardSize);
                break;
        }
    }
}
