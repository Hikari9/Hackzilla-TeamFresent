package com.fresent.fresent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.base.BindToolbar;
import com.fresent.fresent.camera.CameraActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

@BindContentView(R.layout.activity_main)
@BindToolbar(R.id.toolbar)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN";
    private static final int REQUEST_CAMERA = 1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        Log.d(TAG, "Starting camera activity");
        startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }

    // TODO: do face recognition stuff on receive image from camera activity
    protected void onReceiveImage(Bitmap bitmap) {
        // convert Bitmap to Mat for OpenCV
        Snackbar.make(getWindow().getDecorView(), "TODO: face detection", Snackbar.LENGTH_LONG)
            .show();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
