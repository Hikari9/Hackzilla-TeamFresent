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
@Table(name = "student_enrollment")
public interface StudentEnrollment extends Parcelable, Persistable {

    @Key
    @Generated
    @Column(name = "id")
    int getId();

    @ForeignKey(referencedColumn = "id_number")
    @ManyToOne
    Student getStudent();

    @ForeignKey(referencedColumn = "id")
    @ManyToOne
    Class getClassEntity();

    @OneToMany
    Result<AttendanceCheck> getAttendanceChecks();

}
