package com.fresent.fresent.student_attendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import butterknife.BindView;
import io.reactivex.functions.Consumer;

@BindContentView(R.layout.activity_fresent)
public class FresentActivity extends BaseActivity {

    private static final int REQUEST_CAMERA = 1;

    @BindView(R.id.studentList)
    ListView studentListView;
    private List<StudentEntity> models;
    private ArrayAdapter<StudentEntity> listAdapter;

    @BindView(R.id.jumbotron)
    ImageView jumbotron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.models = new ArrayList<StudentEntity>();
        this.listAdapter = new StudentListAdapter(getApplicationContext(), this.models);
        this.studentListView.setAdapter(listAdapter);

        if (getIntent() != null && getIntent().hasExtra("data")) {
            Intent from = getIntent();
            ClassEntity commonClass = from.getParcelableExtra("data");
            fillStudentModels(commonClass);
        } else {
            // just get one class
            database()
                .select(ClassEntity.class)
                .limit(1)
                .get()
                .flowable()
                .subscribe(new Consumer<ClassEntity>() {
                    @Override
                    public void accept(ClassEntity commonClass) {
                        fillStudentModels(commonClass);
                    }
                });
        }

        startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == CameraActivity.RESULT_DENIED) {
                // show snackbar that camera permission is required
                Snackbar.make(findViewById(R.id.coordinator_layout), "Camera is required to take attendance", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Allow", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // request camera again
                            startActivityForResult(new Intent(FresentActivity.this, CameraActivity.class), REQUEST_CAMERA);
                        }
                    }).show();
            } else if (resultCode == CameraActivity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onReceiveImage(bitmap);
            }
        }

    }

    private void onReceiveImage(Bitmap bitmap) {
        this.jumbotron.setImageBitmap(bitmap);
    }

    private void fillStudentModels(ClassEntity classEntity) {
        List<StudentEnrollment> list = classEntity.getStudentEnrollments().toList();
        for (StudentEnrollment enrollment : list) {
            models.add((StudentEntity) enrollment.getStudent());
        }
        listAdapter.notifyDataSetChanged();
    }

}
