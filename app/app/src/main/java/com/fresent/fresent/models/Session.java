package com.fresent.fresent.models;

import android.os.Parcelable;

import java.sql.Date;

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
@Table(name = "session")
public interface Session extends Parcelable, Persistable {

    String UNCONFIRMED = "UNCONFIRMED";
    String OK = "OK";

    @Key
    @Column(name = "id")
    int getId();

    @ForeignKey(referencedColumn = "id", references = Class.class)
    @Column(name = "class_id")
    @ManyToOne
    Class getClassEntity();

    @Column(name = "session_date")
    Date getDate();

    @OneToMany
    Result<Image> getImages();

    @OneToMany
    Result<AttendanceCheck> getAttendanceChecks();

}
