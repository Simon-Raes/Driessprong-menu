package be.driessprong.menu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class MenuItem implements Parcelable {
    protected String name, photo;

    public MenuItem(){

    }

    public MenuItem(String name, String photo){
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    private MenuItem(Parcel in) {
        this.name = in.readString();
        this.photo = in.readString();
    }

}
