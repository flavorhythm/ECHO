package data;

/**
 * Created by ZYuki on 7/7/2016.
 */
public class Specs {
    private String label;
    private String value;

    public Specs() {}

    public Specs(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public void setLabel(String label) {this.label = label;}
    public void setValue(String value) {this.value = value;}

    public String getLabel() {return label;}
    public String getValue() {return value;}
}
