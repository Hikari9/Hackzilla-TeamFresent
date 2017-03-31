package com.fresent.fresent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.base.BindToolbar;
import com.fresent.fresent.camera.CameraActivity;
import com.fresent.fresent.classes.ClassListAdapter;
import com.fresent.fresent.models.ClassEntity;
import com.fresent.fresent.models.StudentEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.requery.util.function.Consumer;

@BindContentView(R.layout.activity_main)
@BindToolbar(R.id.toolbar)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN";
    private static final int REQUEST_CAMERA = 1;

    private static ArrayAdapter<ClassEntity> listAdapter;

    private List<ClassEntity> classModels;
    private ListView classListView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Example of a call to a native method
        /*
        final TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        // test database query: "SELECT COUNT(*) FROM student"
        database()
            .count(StudentEntity.class)
            .get()
            .consume(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                Toast.makeText(getApplicationContext(),
                    String.valueOf(integer) + " students in database", Toast.LENGTH_LONG)
                    .show();
            }
        });

        */


        setContentView(R.layout.activity_main);

        // FAB click listener
        final FloatingActionButton newClassFab = (FloatingActionButton) findViewById(R.id.newClassFab);
        newClassFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.gotoAddClassEntityActivity( v );
            }
        });

        // List View
        this.classModels = new ArrayList<ClassEntity>();
        this.listAdapter = new ClassListAdapter( getApplicationContext(), this.classModels );
        this.classListView = (ListView) findViewById( R.id.classListView );
        this.classListView.setAdapter( listAdapter );
        this.classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassEntity model = listAdapter.getItem( position );
                MainActivity.this.gotoClassViewActivity( model );
            }
        });

        // Fetching the Models
        fillMockData();
    }

    private void fillMockData() {
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section A", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section B", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section C", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section CC", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section D", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section E", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section F", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section F1", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section G", "2016 - 2017", "2nd Sem") );
        this.classModels.add( createClass("Introduction to CC", "CC101", "Section HH", "2016 - 2017", "2nd Sem") );
        this.listAdapter.notifyDataSetChanged();
    }

    private void fillClassModel() {
        database().select(ClassEntity.class)
                .get()
                .flowable()
                .subscribe(new io.reactivex.functions.Consumer<ClassEntity>() {
                    @Override
                    public void accept(ClassEntity classEntity) throws Exception {
                        MainActivity.this.classModels.add( classEntity );
                        MainActivity.this.listAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void gotoAddClassEntityActivity(View v) {
        Toast.makeText(getApplicationContext(),
                "FAB tapped", Toast.LENGTH_SHORT)
                .show();
    }

    private void gotoClassViewActivity(ClassEntity model) {
        Toast.makeText(getApplicationContext(),
                "huwag mahihiyang magtanong", Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.newClassFab)
    public void onClickFab(View view) {
        /*
        Log.d(TAG, "Starting camera activity");
        startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CAMERA);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == CameraActivity.RESULT_DENIED) {
                // show snackbar that camera permission is required
                Snackbar.make(getWindow().getDecorView(),
                    "Camera is required to take pictures",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Allow", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // request camera again
                            onClickFab(v);
                        }
                    })
                    .show();
            } else if (resultCode == CameraActivity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onReceiveImage(bitmap);
            }
        }
        */
    }

    // TODO: do face recognition stuff on receive image from camera activity
    protected void onReceiveImage(Bitmap bitmap) {
        /*
        Snackbar.make(getWindow().getDecorView(), "TODO: face detection", Snackbar.LENGTH_LONG)
            .show();
        */
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
