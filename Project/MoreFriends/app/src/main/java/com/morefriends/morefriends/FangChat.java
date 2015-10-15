package com.morefriends.morefriends;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Kjye on 10/15/2015.
 */
public class FangChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "cFWnuRjdEE2TJY7DZvjJmKMGDyBWZLpzuKDuWwN7", "r6fDFWSXCX4V9hGXS5xbvCOjEXwnUd64sKPk8c7v");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

}
