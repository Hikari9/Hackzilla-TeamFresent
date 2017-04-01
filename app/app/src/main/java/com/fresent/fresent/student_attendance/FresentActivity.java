package com.fresent.fresent.student_attendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fresent.fresent.R;
import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.camera.CameraActivity;
import com.fresent.fresent.models.ClassEntity;
import com.fresent.fresent.models.StudentEnrollment;
import com.fresent.fresent.models.StudentEntity;

import java.util.ArrayList;
import java.util.List;

import io.requery.util.function.Consumer;

@BindContentView(R.layout.activity_fresent)
public class FresentActivity extends BaseActivity {

    private static final int REQUEST_CAMERA = 1;

    private ListView studentListView;
    private List<StudentEntity> models;
    private ArrayAdapter<StudentEntity> listAdapter;

    private ImageView jumbotron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.jumbotron = (ImageView) findViewById(R.id.jumbotron);

        Intent from = getIntent();
        ClassEntity commonClass = from.getParcelableExtra("data");

        this.models = new ArrayList<StudentEntity>();
        this.listAdapter = new StudentListAdapter( getApplicationContext(), this.models );
        this.studentListView = (ListView) findViewById( R.id.studentList );
        this.studentListView.setAdapter( listAdapter );
        fillStudentModels( commonClass );

        startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == CameraActivity.RESULT_DENIED) {
                // show snackbar that camera permission is required
                Toast.makeText(getApplicationContext(),
                        "App not allowed to use camera", Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else if (resultCode == CameraActivity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onReceiveImage(bitmap);
            }
        }

    }

    private void onReceiveImage(Bitmap bitmap) {
        this.jumbotron.setImageBitmap( bitmap );
    }

    private void fillStudentModels(ClassEntity c) {
        c.getStudentEnrollments()
                .each(new Consumer<StudentEnrollment>() {
                    @Override
                    public void accept(StudentEnrollment studentEnrollment) {
                        FresentActivity.this.models.add( (StudentEntity) studentEnrollment.getStudent() );
                        FresentActivity.this.listAdapter.notifyDataSetChanged();
                    }
                });
    }

}
