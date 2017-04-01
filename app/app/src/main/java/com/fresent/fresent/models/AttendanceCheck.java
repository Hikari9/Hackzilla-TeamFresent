package com.fresent.fresent.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;
import io.requery.query.Result;

@Entity
@Table(name = "attendance_check")
public interface AttendanceCheck extends Parcelable, Persistable {

    String UNCONFIRMED = "UNCONFIRMED";

    @Key
    @Column(name = "id")
    int getId();

    @ForeignKey(referencedColumn = "id", references = StudentEnrollment.class)
    @Column(name = "student_enrollment_id")
    @ManyToOne
    StudentEnrollment getStudentEnrollment();

    @ForeignKey(referencedColumn = "id", references = Session.class)
    @Column(name = "session_id")
    @ManyToOne
    Session getSession();

    @Column(name = "attendance_status")
    String getAttendanceStatus();

    @OneToMany
    Result<Image> getImages();

}
