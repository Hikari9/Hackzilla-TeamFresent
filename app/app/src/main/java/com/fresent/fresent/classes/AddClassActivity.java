package com.fresent.fresent.classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fresent.fresent.R;
import com.fresent.fresent.base.BaseActivity;
import com.fresent.fresent.base.BindContentView;
import com.fresent.fresent.models.AttendanceCheck;
import com.fresent.fresent.models.AttendanceCheckEntity;
import com.fresent.fresent.models.ClassEntity;
import com.fresent.fresent.models.Student;
import com.fresent.fresent.models.StudentEnrollment;
import com.fresent.fresent.models.StudentEnrollmentEntity;
import com.fresent.fresent.models.StudentEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindString;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.functions.Consumer;
import io.requery.query.Condition;

@BindContentView(R.layout.activity_add_class)
public class AddClassActivity extends BaseActivity {

    public static final String TAG = "DATA";
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

    private void onAcceptRequest(String response) {

        final JSONObject json;

        try {
            json = new JSONObject(response);

            // convert this to a class
            ClassEntity cls = new ClassEntity();
            // cls.setId(json.getInt("id"));
            cls.setName(json.getString("name"));
            cls.setCourseCode(json.getString("course_code"));
            cls.setSection(json.getString("section"));
            cls.setSchoolYear("" + json.getInt("school_year"));
            cls.setSchoolTerm("" + json.getInt("school_term"));

            database().upsert(cls)
                .subscribe(new Consumer<ClassEntity>() {
                    @Override
                    public void accept(final ClassEntity classEntity) throws Exception {

                        Log.i(TAG, "Updated Class: " + classEntity);

                        JSONArray students = json.getJSONArray("students");

                        for (int i = 0; i < students.length(); ++i) {
                            JSONObject obj = students.getJSONObject(i);
                            StudentEntity stud = new StudentEntity();
                            stud.setIdNumber("" + obj.getInt("id"));
                            stud.setFirstName(obj.getString("first_name"));
                            stud.setMiddleName(obj.getString("middle_name"));
                            stud.setLastName(obj.getString("last_name"));
                            stud.setIdPicture(obj.getString("id_picture"));
                            database().upsert(stud).subscribe(new Consumer<StudentEntity>() {
                                @Override
                                public void accept(StudentEntity studentEntity) throws Exception {
                                    Log.i(TAG, "Updated student: " + studentEntity);
                                    String studentId = studentEntity.getIdNumber();
                                    int classId = classEntity.getId();
                                    List<StudentEnrollmentEntity> results = database()
                                        .select(StudentEnrollmentEntity.class)
                                        .where(StudentEnrollmentEntity.CLASS_ENTITY_ID.equal(classId))
                                        .and(StudentEnrollmentEntity.STUDENT_ID.like(studentId))
                                        .get()
                                        .toList();
                                    if (results.size() == 0) {
                                        // insert new
                                        StudentEnrollmentEntity enrollment = new StudentEnrollmentEntity();
                                        enrollment.setClassEntity(classEntity);
                                        enrollment.setStudent(studentEntity);
                                        database().insert(enrollment).subscribe(new Consumer<StudentEnrollmentEntity>() {
                                            @Override
                                            public void accept(StudentEnrollmentEntity studentEnrollmentEntity) throws Exception {
                                                Log.i(TAG, "Updated Student Enrollment: " + studentEnrollmentEntity);
                                            }
                                        });
                                    } else {
                                        Log.i(TAG, "Reused Student Enrollment: " + results.get(0));
                                    }
                                }
                            });
                        }

                    }
                });


        } catch (JSONException e) {
            Toast.makeText(this, "Error in request", Toast.LENGTH_SHORT).show();
            Log.e("REQUEST", e.getMessage(), e);
            e.printStackTrace();
            return;
        }


        Toast.makeText(AddClassActivity.this,
            "Successfully loaded class", // TODO: add class code and num of students
            Toast.LENGTH_LONG).show();






    }

    /**
     * Handles the dispatching of QR Code
     * @param contents
     */
    private void handleQRCode(final String contents) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + contents;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    onAcceptRequest(response);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.e("SERVER_ERROR", error.toString());
                Snackbar.make(getWindow().getDecorView(),
                    "Server error occured" + error.toString(),
                    Snackbar.LENGTH_LONG)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // try sending request again
                            handleQRCode(contents);
                        }
                    })
                    .show();
            }
        });

        stringRequest.setTag("CLASS_REQUEST");
        queue.add(stringRequest);
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
