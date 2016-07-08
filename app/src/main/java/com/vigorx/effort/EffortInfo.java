package com.vigorx.effort;


import android.os.Parcel;
import android.os.Parcelable;

public class EffortInfo implements Parcelable {
    public static final int EFFORT_SIZE = 28;
    private int id;
    private String title;
    private String startDate;
    private int haveAlarm;
    private String alarm;
    private int status[] = new int[EFFORT_SIZE];

    public EffortInfo() {
        return;
    }

    protected EffortInfo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        startDate = in.readString();
        haveAlarm = in.readInt();
        alarm = in.readString();
        status = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(startDate);
        dest.writeInt(haveAlarm);
        dest.writeString(alarm);
        dest.writeIntArray(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EffortInfo> CREATOR = new Creator<EffortInfo>() {
        @Override
        public EffortInfo createFromParcel(Parcel in) {
            return new EffortInfo(in);
        }

        @Override
        public EffortInfo[] newArray(int size) {
            return new EffortInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getHaveAlarm() {
        return haveAlarm;
    }

    public void setHaveAlarm(int haveAlarm) {
        this.haveAlarm = haveAlarm;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public int[] getStatus() {
        return status;
    }

    public void setStatus(int[] status) {
        this.status = status;
    }
}
