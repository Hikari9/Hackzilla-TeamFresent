package com.fresent.fresent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fresent.fresent.ai.FaceDetection;
import com.fresent.fresent.ai.FaceRecognition;
import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.base.BindToolbar;
import com.fresent.fresent.camera.CameraActivity;

import org.opencv.core.Rect;

import butterknife.OnClick;

@BindContentView(R.layout.activity_main)
@BindToolbar(R.id.toolbar)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN";
    private static final int REQUEST_CAMERA = 1;
    FaceDetection faceDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faceDetection = new FaceDetection(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (faceDetection.getViolaJonesClassifier() == null)
            faceDetection.loadOpenCV();
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        Log.d(TAG, "Starting camera activity");
        startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == CameraActivity.RESULT_DENIED) {
                // show snackbar that camera permission is required
                Snackbar.make(findViewById(R.id.coordinator_layout),
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: do face recognition stuff on receive image from camera activity
    protected void onReceiveImage(Bitmap bitmap) {
        // convert Bitmap to Mat for OpenCV
        Rect[] faces = faceDetection.detectFaces(bitmap);
        Toast.makeText(this, "Detected faces: " + faces.length, Toast.LENGTH_LONG).show();
    }

}
