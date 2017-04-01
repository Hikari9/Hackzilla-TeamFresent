package com.fresent.fresent.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;
import io.requery.query.Result;

@Entity
@Table(name = "class")
public interface Class extends Parcelable, Persistable {

    @Key
    @Column(name = "id")
    int getId();

    @Column(name = "name")
    String getName();

    @Column(name = "course_code")
    String getCourseCode();

    @Column(name = "section")
    String getSection();

    @Column(name = "school_year")
    String getSchoolYear();

    @Column(name = "school_term")
    String getSchoolTerm();

    @OneToMany
    Result<StudentEnrollment> getStudentEnrollments();

}
