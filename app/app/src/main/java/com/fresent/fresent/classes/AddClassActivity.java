package com.fresent.fresent.classes;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fresent.fresent.R;
import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;

import butterknife.BindString;
import butterknife.OnClick;
import butterknife.OnItemClick;

@BindContentView(R.layout.activity_add_class)
public class AddClassActivity extends BaseActivity {

    private static final int REQUEST_QR_CODE = 1;

    private EditText name, code, section, term, sy, qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.name = (EditText) findViewById(R.id.classNameEditText);
        this.code = (EditText) findViewById(R.id.classCodeEditText);
        this.section = (EditText) findViewById(R.id.classSectionEditText);
        this.term = (EditText) findViewById(R.id.classTermEditText);
        this.sy = (EditText) findViewById(R.id.classSchoolYearEditText);
        this.qr = (EditText) findViewById(R.id.classQREditText);
    }

    @OnClick(R.id.qrScan)
    protected void gotoQRScanner(View v) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, REQUEST_QR_CODE);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            Toast.makeText(this, "Please install Google QR Code Scanner for this feature", Toast.LENGTH_LONG)
                .show();
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QR_CODE) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                handleQRCode(contents);
            }
        }
    }

    /**
     * Handles the dispatching of QR Code
     * @param contents
     */
    private void handleQRCode(final String contents) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddClassActivity.this, contents, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.addNewClass)
    protected void addNewClass(View v) {
        if( this.code.getText().toString().length() == 0 ||
                this.section.getText().toString().length() == 0 ||
                this.qr.getText().toString().length() == 0)
            return;

        String[] value = new String[] {
                this.name.getText().toString(),
                this.code.getText().toString(),
                this.section.getText().toString(),
                this.term.getText().toString(),
                this.sy.getText().toString(),
                this.qr.getText().toString()
        };

        Intent toReturn = new Intent();
        toReturn.putExtra("data", value);
        setResult(RESULT_OK, toReturn);
        finish();
    }
}
