package com.fresent.fresent.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Delegate activity for camera permission and snapping a picture
 */
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CAMERA";
    private static final int REQUEST_CAMERA = 1;
    public static final int RESULT_DENIED = 129836; // random number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchCameraIntent();
    }

    /**
     * Requesting permissions to use camera
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    launchCameraIntent();
                } else {
                    // show a snackbar in parent
                    setResult(RESULT_DENIED);
                    finish();
                }
        }
    }

    public void launchCameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // request for permission if necessary (required in Marshmallow above)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Log.d(TAG, "This device does not have a camera.");
                finishActivity(RESULT_DENIED);
                return;
            }
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            setResult(resultCode, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
