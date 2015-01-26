package be.driessprong.menu.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import be.driessprong.menu.R;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class MenuApplication extends Application {

    @Override
    public void onCreate() {

        // Register ParseObject subclasses
//        ParseObject.registerSubclass(DayLoader.class);
        // Initialize Parse
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));

        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
