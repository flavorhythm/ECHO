package data;

/**
 * Created by zyuki on 6/1/2016.
 */
public class Card {
    public static final int CARD_TYPE_HOME = 0;
    public static final int CARD_TYPE_GUIDE = 1;
    public static final int CARD_TYPE_DOC = 2;
    public static final int CARD_TYPE_MAINT = 3;
    public static final int CARD_TYPE_SPECS = 4;

    public static final int CARD_SIZE_SLIM = 0;
    public static final int CARD_SIZE_SMALL = 1;
    public static final int CARD_SIZE_LARGE = 2;

    public static final int CARD_TYPE_PLACEHOLDER = 99;

    int cardType;
    int cardSize;

    int drawableRes;
    String cardTitle, cardSubtitle;

    public Card() {}

    public Card(int cardType, int cardSize, int drawableRes, String cardTitle) {
        this.cardType = cardType;
        this.cardSize = cardSize;
        this.drawableRes = drawableRes;
        this.cardTitle = cardTitle;
    }

    public Card(int cardType, int cardSize, int drawableRes, String cardTitle, String cardSubtitle) {
        this.cardType = cardType;
        this.cardSize = cardSize;
        this.drawableRes = drawableRes;
        this.cardTitle = cardTitle;
        this.cardSubtitle = cardSubtitle;
    }

    public void setCardType(int cardType) {this.cardType = cardType;}
    public void setCardSize(int cardSize) {this.cardSize = cardSize;}
    public void setDrawableRes(int drawableRes) {this.drawableRes = drawableRes;}
    public void setCardTitle(String cardTitle) {this.cardTitle = cardTitle;}
    public void setCardSubtitle(String cardSubtitle) {this.cardSubtitle = cardSubtitle;}

    public int getCardType() {return cardType;}
    public int getCardSize() {return cardSize;}
    public int getDrawableRes() {return drawableRes;}
    public String getCardTitle() {return cardTitle;}
    public String getCardSubtitle() {return cardSubtitle;}
}