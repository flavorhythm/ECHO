package data.item;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by flavorhythm on 2/2/17.
 */

public class ParcelItemClass extends ParcelDataItem {
    //enums for itemclasses?
        //enum mClassName
        //int mClassId
    //no target layoutid
    private int mClassId;
    private List<ParcelModel> mModelList = new ArrayList<>();

    public ParcelItemClass(String name, int classId, int drawableId, ParcelModel... models) {
        super(name, null, drawableId);
        mClassId = classId;
        setModels(models);
    }

    public ParcelItemClass(String name, int classId, int drawableId, List<ParcelModel> modelList) {
        super(name, null, drawableId);
        mClassId = classId;
        mModelList.addAll(modelList);
    }


    public ParcelItemClass(Parcel src) {
        super(src);
    }

    public String getName() {return super.getContent();}
    public int getClassId() {return mClassId;}
    public int getDrawableId() {return super.getDrawableId();}
    public List<ParcelModel> getModels() {return mModelList;}

    public void setName(String name) {super.setContent(name);}
    public void setClassId(int id) {mClassId = id;}
    public void setDrawableId(int id) {super.setDrawableId(id);}
    public void setModels(ParcelModel... models) {
        //TODO: collection issue?
        for(ParcelModel model : models) {
            addModel(model);
        }
    }
    public void addModel(ParcelModel model) {mModelList.add(model);}

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeInt(mClassId);
        dst.writeParcelableArray((ParcelModel[])mModelList.toArray(), flags);
        super.writeToParcel(dst, flags);
    }

    @Override
    protected int getSubClass() {
        return ITEMCLASS;
    }
}
