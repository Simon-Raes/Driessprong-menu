package be.driessprong.menu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class Soup extends MenuItem implements Parcelable {


    public Soup(String name, String photo) {
        super(name, photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.photo);
    }

    private Soup(Parcel in) {
        this.name = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<Soup> CREATOR = new Parcelable.Creator<Soup>() {
        public Soup createFromParcel(Parcel source) {
            return new Soup(source);
        }

        public Soup[] newArray(int size) {
            return new Soup[size];
        }
    };
}
