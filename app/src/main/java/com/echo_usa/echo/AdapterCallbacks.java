package com.echo_usa.echo;

import android.view.View;

import data.card_content.CardContent;
import data.frag_list.FragListItem;
import data.navigation.NavItem;

/**
 * Created by flavorhythm on 1/24/17.
 */

public interface AdapterCallbacks {
    View.OnClickListener getNavEndItemListener(NavItem item);
    View.OnClickListener getFragListItemListener(FragListItem item);
    View.OnClickListener getCardListItemListener(CardContent item);
    View.OnClickListener getItemViewerListener();
}
