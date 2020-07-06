package com.app.ac_twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("lllkTkrX7stdFDG6V98IsPtRDEkcOXXgjaA0Ezqg")
                .clientKey("xn6q8m2fGR89ELi4BFOTY4ExD1jvENGeNXhpkFFp")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
