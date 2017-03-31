package com.fresent.fresent.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;
import io.requery.query.Result;

@Entity
@Table(name = "student")
public interface Student extends Parcelable, Persistable {

    @Key
    @Column(name = "id_number")
    String getIdNumber();

    @Column(name = "last_name", index = true)
    String getLastName();

    @Column(name = "first_name")
    String getFirstName();

    @Column(name = "middle_name")
    String getMiddleName();

    @Column(name = "id_picture")
    String getIdPicture();

    @OneToMany
    Result<StudentEnrollment> getStudentEnrollments();


}
