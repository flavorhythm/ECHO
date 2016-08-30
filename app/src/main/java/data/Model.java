package data;

/**
 * Created by zyuki on 6/14/2016.
 */
public class Model {
    //TODO: saves image bitmaps here?
    private String modelType;
    private String modelName;
    private String serialNum;

    public Model(String modelName, String serialNum) {
        this.modelName = modelName;
        this.serialNum = serialNum;
    }

    public void setModelType(String modelType) {this.modelType = modelType;}
    public void setSerialNum(String serialNum) {this.serialNum = serialNum;}
    public void setModelName(String modelName) {this.modelName = modelName;}

    public String getModelType() {return modelType;}
    public String getModelName() {return modelName;}
    public String getSerialNum() {return serialNum;}
}
