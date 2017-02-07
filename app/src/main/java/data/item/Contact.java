package data.item;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class Contact extends DataItem {
    private Type mType;
    //URL and/or direct user to target (phone, website, etc)
    public Contact(Type type, String contact, String address, int iconId) {
        super(contact, address, iconId);
        mType = type;
    }

    public Type getType() {return mType;}
    public String getContact() {return super.getContent();}
    public String getAddress() {return super.getSubcontent();}
    public int getIconId() {return super.getDrawableId();}

    public void setType(Type type) {mType = type;}
    public void setContact(String contact) {super.setContent(contact);}
    public void setAddress(String address) {super.setSubcontent(address);}
    public void setIconId(int iconId) {super.setDrawableId(iconId);}

    public enum Type {
        PHONE, URL, EMAIL
    }
}
