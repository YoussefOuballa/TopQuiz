package ma.snrt.topquiz.controller;


import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
    private int mData;

    public int describeContents() {
        return 0;
    }

    /** save object in parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    /** recreate object from parcel */
    private MyParcelable(Parcel in) {
        mData = in.readInt();
    }
}