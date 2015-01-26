package be.driessprong.menu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class MainCourse extends MenuItem implements Parcelable {

    ArrayList<Ingredient> ingredients;

    public MainCourse(){
        super();
        ingredients = new ArrayList<>();
    }

    public MainCourse(String name, String photo){
        super(name, photo);
        ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.ingredients);
        dest.writeString(this.name);
        dest.writeString(this.photo);
    }

    private MainCourse(Parcel in) {
        this.ingredients = (ArrayList<Ingredient>) in.readSerializable();
        this.name = in.readString();
        this.photo = in.readString();
    }

    public static final Creator<MainCourse> CREATOR = new Creator<MainCourse>() {
        public MainCourse createFromParcel(Parcel source) {
            return new MainCourse(source);
        }

        public MainCourse[] newArray(int size) {
            return new MainCourse[size];
        }
    };
}
