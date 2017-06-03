package com.example.aleksei.novoselovaleksei.ui.holders.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class DescriptionExpandableView implements Parcelable {

    private String description;

    public DescriptionExpandableView(String description) {
        this.description = description;
    }

    private DescriptionExpandableView(Parcel in) {
        description = in.readString();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
    }

    public static final Creator<DescriptionExpandableView> CREATOR = new Creator<DescriptionExpandableView>() {
        @Override
        public DescriptionExpandableView createFromParcel(Parcel in) {
            return new DescriptionExpandableView(in);
        }

        @Override
        public DescriptionExpandableView[] newArray(int size) {
            return new DescriptionExpandableView[size];
        }
    };
}
