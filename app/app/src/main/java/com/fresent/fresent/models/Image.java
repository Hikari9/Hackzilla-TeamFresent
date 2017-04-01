package com.fresent.fresent.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;

@Entity
@Table(name = "image")
public interface Image extends Parcelable, Persistable {

    @Key
    @Column(name = "id")
    int getId();

    @ForeignKey(referencedColumn = "id", references = Session.class)
    @Column(name = "session_id")
    @ManyToOne
    Session getSession();

    @ForeignKey(referencedColumn = "id", references = AttendanceCheck.class)
    @Column(name = "attendance_check_id")
    @ManyToOne
    AttendanceCheck getAttandanceCheck();

}
