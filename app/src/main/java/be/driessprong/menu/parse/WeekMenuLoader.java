package be.driessprong.menu.parse;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.driessprong.menu.model.Day;
import be.driessprong.menu.model.Ingredient;
import be.driessprong.menu.model.MainCourse;
import be.driessprong.menu.model.Soup;
import be.driessprong.menu.util.DateUtils;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class WeekMenuLoader {

    private MenuCallback delegate;

    public interface MenuCallback {
        public void foundMenu(ArrayList<Day> days);
    }

    public WeekMenuLoader(MenuCallback delegate) {
        this.delegate = delegate;
    }

    public void findInBackground() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Day");

        // Get the menu for this week.
        query.whereGreaterThan("Date", DateUtils.getFirstDateOfWeek());
        query.whereLessThan("Date", DateUtils.getLastDateOfWeek());

        // Also load all contained objects.
        query.include("Soup");
        query.include("Main_course");
        query.include("Main_course.Ingredients");

        query.findInBackground(new FindCallback<ParseObject>() {

            ArrayList<Day> days = new ArrayList<>();

            @Override
            public void done(List<ParseObject> parseDays, ParseException e) {
                if (e == null) {

                    for (ParseObject parseDay : parseDays) {

                        Date date = parseDay.getDate("Date");
                        String title = parseDay.getString("Title");
                        String photo = parseDay.getString("Photo");
                        Soup soup = new Soup(parseDay.getParseObject("Soup").getString("Name"), parseDay.getParseObject("Soup").getString("Photo"));
                        MainCourse mainCourse = new MainCourse();
                        mainCourse.setName(parseDay.getParseObject("Main_course").getString("Name"));
                        mainCourse.setPhoto(parseDay.getParseObject("Main_course").getString("Photo"));
                        List<ParseObject> ingredients = parseDay.getParseObject("Main_course").getList("Ingredients");

                        if (ingredients!=null&&ingredients.size() > 0) {
                            for (ParseObject ingredient : ingredients) {
                                mainCourse.addIngredient(new Ingredient(ingredient.getString("Name")));
                            }
                        }

                        Day day = new Day(date, title, photo, soup, mainCourse);
                        days.add(day);

                    }

                    delegate.foundMenu(days);
                }
            }
        });
    }


}