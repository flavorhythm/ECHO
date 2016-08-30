package adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import widget.CardDivider;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class DividerHolder extends BaseHolder {
    CardDivider cardDivider;

    public DividerHolder(View divider) {
        super(divider);
        cardDivider = (CardDivider)divider;
    }

    public void setDividerText(String title, String subtitle) {
        cardDivider.setDividerText(title, subtitle);
    }

    public void setTopSpacer() {
        cardDivider.addSpacer();
    }
}
