package data.navigation;

import util.FragSpec;

/**
 * Created by zyuki on 6/14/2016.
 */
public class Model extends NavItem {
    private int mItemClass;

    public Model(int imgRes, String modelName, String serialNum, int itemClass) {
        super(TYPE_MODEL, FragSpec.MODEL_DOCS, imgRes, modelName, serialNum);
        mItemClass = itemClass;
    }

    public int getItemClass() {return mItemClass;}
    public String getModelName() {return mContent;}
    public String getSerialNum() {return mSubcontent;}
    public int getImgResource() {return mImgRes;}
}
