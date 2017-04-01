package com.fresent.fresent;

import android.app.Application;
import android.os.StrictMode;

import com.fresent.fresent.models.Models;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

public class MainApplication extends Application {

    private static final int DATABASE_VERSION = 1;
    private static final TableCreationMode CREATION_MODE = TableCreationMode.DROP_CREATE;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
    }

    private ReactiveEntityStore<Persistable> database;
    public ReactiveEntityStore<Persistable> getDatabase() {
        if (database == null) {
            // create database here
            DatabaseSource source = new DatabaseSource(
                getApplicationContext(),
                Models.DEFAULT,
                DATABASE_VERSION);
            if (BuildConfig.DEBUG)
                source.setTableCreationMode(CREATION_MODE);
            database = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(source.getConfiguration()));
        }
        return database;
    }

}
