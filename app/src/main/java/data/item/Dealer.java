package data.item;

/**
 * Created by flavorhythm on 2/3/17.
 */

public class Dealer extends DataItem {
    private String mPhone;
    private String mUrl;
    private float mLat, mLon;
    private float mDistance;

    private boolean mRepairWarrantyService;
    private boolean mSignatureEliteDealer;

    public Dealer(String name, String address, String phone, String url, float lat, float lon,
                  boolean rws, boolean sed) {
        super(name, address, NONE);
        mPhone = phone;
        mUrl = url;
        mLat = lat;
        mLon = lon;

        mRepairWarrantyService = rws;
        mSignatureEliteDealer = sed;
    }

    public String getName() {return super.getContent();}
    public String getAddress() {return super.getSubcontent();}
    public String getPhone() {return mPhone;}
    public String getUrl() {return mUrl;}
    public float getLat() {return mLat;}
    public float getLon() {return mLon;}
    public float getDistance() {return mDistance;}

    public void setName(String name) {super.setContent(name);}
    public void setAddress(String address) {super.setSubcontent(address);}
    public void setPhone(String phone) {mPhone = phone;}
    public void setUrl(String url) {mUrl = url;}
    public void setLat(float lat) {mLat = lat;}
    public void setLon(float lon) {mLon = lon;}
    public void setDistance(float distance) {mDistance = distance;}
}
