package com.fresent.fresent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.base.BindToolbar;
import com.fresent.fresent.classes.AddClassActivity;
import com.fresent.fresent.classes.MainClassListAdapter;
import com.fresent.fresent.drawer.DrawerFactory;
import com.fresent.fresent.models.ClassEntity;
import com.fresent.fresent.student_attendance.FresentActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

@BindContentView(R.layout.main_activity)
@BindToolbar(R.id.toolbar)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN";
    private static final int REQUEST_ADD_CLASS = 1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.main_class_list)
    RecyclerView recyclerView;
    MainClassListAdapter adapter;

    @BindView(R.id.no_classes)
    View noClassesView;
    private Drawer drawer;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD_CLASS: {
                if (resultCode == RESULT_OK) {
                    ClassEntity entity = data.getParcelableExtra("data");
                    Toast.makeText(this, "Added entity: " + entity.toString(), Toast.LENGTH_LONG).show();
                    adapter.addItem(entity);
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawer = DrawerFactory.createDrawerFor(this);
        Button addNewClass = ButterKnife.findById(this, R.id.button_add_class);
        addNewClass.setText(" " + addNewClass.getText());
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                return onDrawerItemClick(view, position, drawerItem);
            }
        });
        adapter = new MainClassListAdapter();
        recyclerView.setAdapter(adapter);
        refreshData();
    }

    public void refreshData() {
        adapter.clearList();
        database().select(ClassEntity.class)
            .get()
            .flowable()
            .subscribe(new Consumer<ClassEntity>() {
                @Override
                public void accept(ClassEntity classEntity) throws Exception {
                    adapter.addItem(classEntity);
                    Log.d(TAG, "Accepting: " + classEntity);
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        if (recyclerView.getChildCount() == 0) {
            noClassesView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noClassesView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    protected boolean onDrawerItemClick(View view, int position, IDrawerItem drawerItem) {
        switch ((int) drawerItem.getIdentifier()) {
            /*
            case R.id.nav_gallery: {
                Intent intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                break;
            }
            */
            case R.id.nav_students: {
                Intent intent = new Intent(this, FresentActivity.class);
                startActivity(intent);
                break;
            }
        }
        drawer.setSelection(R.id.nav_home, false);
        return false;
    }

    @OnClick(R.id.button_add_class)
    protected void onAddClass(View v) {
        Intent intent = new Intent(this, AddClassActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    protected void onClickFab(View v) {
        Intent intent = new Intent(this, FresentActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
