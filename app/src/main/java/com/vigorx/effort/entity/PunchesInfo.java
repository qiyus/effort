package com.vigorx.effort.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by songlei on 16/7/11.
 */
public class PunchesInfo implements Parcelable {
    private int id;
    private int effortId;
    private int offset;
    private int complete;

    public PunchesInfo() {

    }

    public PunchesInfo(Parcel in) {
        id = in.readInt();
        effortId = in.readInt();
        offset = in.readInt();
        complete = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(effortId);
        dest.writeInt(offset);
        dest.writeInt(complete);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PunchesInfo> CREATOR = new Creator<PunchesInfo>() {
        @Override
        public PunchesInfo createFromParcel(Parcel in) {
            return new PunchesInfo(in);
        }

        @Override
        public PunchesInfo[] newArray(int size) {
            return new PunchesInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEffortId() {
        return effortId;
    }

    public void setEffortId(int effortId) {
        this.effortId = effortId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

}
