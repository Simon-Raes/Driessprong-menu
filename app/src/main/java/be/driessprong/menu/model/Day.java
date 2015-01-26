package be.driessprong.menu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Simon Raes on 26/01/2015.
 */

public class Day implements Parcelable {
    private String title, photo;
    private Soup soup;
    private MainCourse mainCourse;

    public Day(String title, String photo, Soup soup, MainCourse mainCourse){
        this.title = title;
        this.photo = photo;
        this.soup = soup;
        this.mainCourse = mainCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Soup getSoup() {
        return soup;
    }

    public void setSoup(Soup soup) {
        this.soup = soup;
    }

    public MainCourse getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(MainCourse mainCourse) {
        this.mainCourse = mainCourse;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeParcelable(this.soup, flags);
        dest.writeParcelable(this.mainCourse, flags);
    }

    private Day(Parcel in) {
        this.title = in.readString();
        this.photo = in.readString();
        this.soup = in.readParcelable(Soup.class.getClassLoader());
        this.mainCourse = in.readParcelable(MainCourse.class.getClassLoader());
    }

    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
