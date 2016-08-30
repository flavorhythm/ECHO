package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.List;

import adapter.viewholder.CardHolder;
import adapter.viewholder.DividerHolder;
import data.Card;
import data.Contact;
import widget.CardDivider;
import widget.EchoCard;

/**
 * Created by zyuki on 8/1/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private int layoutRes;
    private List<Contact> contactList;
    private Resources resources;

    private static final int CONTACT_VIEW_TYPE = 0;
    private static final int DIVIDER_VIEW_TYPE = 1;

    private static final int DIVIDER1_POS = 0;
    private static final int DIVIDER2_POS = 5;
    private static final int DIVIDER3_POS = 7;

    public ContactAdapter(Context context, int layoutRes) {
        inflater = LayoutInflater.from(context);

        this.resources = context.getResources();
        this.layoutRes = layoutRes;
        this.contactList = builtContactList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        switch(viewType) {
            case DIVIDER_VIEW_TYPE: return new DividerHolder(new CardDivider(context));
            default: return new CardHolder(new EchoCard(parent.getContext(), Card.CARD_SIZE_SLIM));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CardHolder) {
            Contact thisContactObj = contactList.get(getAdjustedPos(position));

            ((CardHolder)holder).cardImage.setImageResource(thisContactObj.getIconRes());
            ((CardHolder)holder).cardTitle.setText(thisContactObj.getLabel());
            ((CardHolder)holder).cardSubtitle.setText(thisContactObj.getContentText());
        } else {
            Contact nextContactObj = contactList.get(getAdjustedPos(position) + 1);

            String title = "";
            switch(nextContactObj.getContactType()) {
                case Contact.ECHO_CONTACT: title = "Contact Us"; break;
                case Contact.APP_CONTACT: title = "App Feedback"; break;
                case Contact.V58_CONTACT: title = "58V Product Info"; break;
            }
            ((DividerHolder)holder).setDividerText(title, null);
        }
    }

    private int getAdjustedPos(int originalPos) {
        if(originalPos == 0) return 0;
        else if(originalPos < DIVIDER2_POS) return originalPos - 1;
        else if(originalPos < DIVIDER3_POS) return originalPos - 2;
        else return originalPos - 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == DIVIDER1_POS) return DIVIDER_VIEW_TYPE;
        else if(position < DIVIDER2_POS) return CONTACT_VIEW_TYPE;
        else if(position == DIVIDER2_POS) return DIVIDER_VIEW_TYPE;
        else if(position < DIVIDER3_POS) return CONTACT_VIEW_TYPE;
        else if(position == DIVIDER3_POS) return DIVIDER_VIEW_TYPE;
        else return CONTACT_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return contactList.size() + 3;
    }

    private List<Contact> builtContactList() {
        List<Contact> contactList = new ArrayList<>();

        contactList.add(
                new Contact(
                        Contact.ECHO_CONTACT,
                        R.drawable.ic_vector_http,
                        resources.getString(R.string.label_web),
                        resources.getString(R.string.echo_URL)
                )
        );
        contactList.add(
                new Contact(
                        Contact.ECHO_CONTACT,
                        R.drawable.ic_vector_address,
                        resources.getString(R.string.label_address),
                        resources.getString(R.string.echo_address)
                )
        );
        contactList.add(
                new Contact(
                        Contact.ECHO_CONTACT,
                        R.drawable.ic_vector_phone,
                        resources.getString(R.string.label_phone),
                        resources.getString(R.string.echo_phone)
                )
        );
        contactList.add(
                new Contact(
                        Contact.ECHO_CONTACT,
                        R.drawable.ic_vector_chat,
                        resources.getString(R.string.label_chat),
                        resources.getString(R.string.chat_URL)
                )
        );
        contactList.add(
                new Contact(
                        Contact.APP_CONTACT,
                        R.drawable.ic_vector_email,
                        resources.getString(R.string.label_app_email),
                        resources.getString(R.string.fedback_email)
                )
        );
        contactList.add(
                new Contact(
                        Contact.V58_CONTACT,
                        R.drawable.ic_vector_http,
                        resources.getString(R.string.label_web),
                        resources.getString(R.string.V58_URL)
                )
        );
        return contactList;
    }

    class ContactUsHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;
        TextView content;

        public ContactUsHolder(View itemView) {
            super(itemView);

            icon = (ImageView)itemView.findViewById(R.id.card_slim_image);
            label = (TextView)itemView.findViewById(R.id.card_slim_title);
            content = (TextView)itemView.findViewById(R.id.card_slim_subtitle);
        }
    }
}
