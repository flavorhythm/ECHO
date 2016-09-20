package data;

/**
 * Created by zyuki on 6/14/2016.
 */
public class Model {
    //TODO: saves image bitmaps here?
    public static final boolean EMPTY_FLAG = true;

    private String modelType;
    private String modelName;
    private String serialNum;
    private int imgResource;

    private boolean empty = false;

    public Model() {}

    public Model(boolean empty) {this.empty = empty;}

    public Model(String modelName, String serialNum, int imgResource) {
        this.modelName = modelName;
        this.serialNum = serialNum;
        this.imgResource = imgResource;
    }

    public boolean isEmpty() {return empty;}

    public void setModelType(String modelType) {this.modelType = modelType;}
    public void setSerialNum(String serialNum) {this.serialNum = serialNum;}
    public void setModelName(String modelName) {this.modelName = modelName;}
    public void setImgResource(int imgResource) {this.imgResource = imgResource;}

    public String getModelType() {return modelType;}
    public String getModelName() {return modelName;}
    public String getSerialNum() {return serialNum;}
    public int getImgResource() {return imgResource;}
}
