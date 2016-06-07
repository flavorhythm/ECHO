package data;

/**
 * Created by zyuki on 6/1/2016.
 */
public class Card {

    int drawableRes;
    String cardText;

    public Card() {}

    public Card(int drawableRes, String cardText) {
        this.drawableRes = drawableRes;
        this.cardText = cardText;
    }

    public void setDrawableRes(int drawableRes) {this.drawableRes = drawableRes;}
    public void setCardText(String cardText) {this.cardText = cardText;}

    public int getDrawableRes() {return drawableRes;}
    public String getCardText() {return cardText;}
}