package com.sourabhkarkal.quandoo;

import android.app.Application;

import io.realm.RealmConfiguration;

/**
 * Created by sourabhkarkal on 14/07/16.
 */
public class ApplicationController extends Application {

    private static ApplicationController mInstance;
    private RealmConfiguration realmConfig = null;
    public static final String DB_DATA = "QuandooData";

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RealmConfiguration getRealmConfig() {
        //realm migration
            if (realmConfig == null) {
                realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                        .name(DB_DATA)
                        .schemaVersion(1)
                        //.migration(new RealmMigration())
                        .deleteRealmIfMigrationNeeded()
                        .build();
            }
        return realmConfig;
    }

}
