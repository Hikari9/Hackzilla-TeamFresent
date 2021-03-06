package com.fresent.fresent.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fresent.fresent.MainApplication;

import java.lang.annotation.AnnotationFormatError;

import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class BaseActivity extends AppCompatActivity {

    private ReactiveEntityStore<Persistable> reactiveEntityStore;

    /**
     * Gets the application's database instance as a reactive entity store powered by requery.
     * @return the database
     */
    public ReactiveEntityStore<Persistable> database() {
        if (reactiveEntityStore == null)
            reactiveEntityStore = ((MainApplication) getApplication()).getDatabase();
        return reactiveEntityStore;
    }


    private Toolbar toolbar;
    public Toolbar getToolbar() {return toolbar;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<?> cls = getClass();
        BindContentView bindContentView = cls.getAnnotation(BindContentView.class);
        if (bindContentView == null) {
            throw new AnnotationFormatError("@BindContentView is required for class "
                + getClass().getCanonicalName());
        }
        setContentView(bindContentView.value());
        BindToolbar bindToolbar = cls.getAnnotation(BindToolbar.class);
        if (bindToolbar != null) {
            toolbar = ButterKnife.findById(this, bindToolbar.value());
            setSupportActionBar(toolbar);
            if (getTitle() != null)
                toolbar.setTitle(getTitle());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(bindToolbar.back());
        }
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // go back
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
