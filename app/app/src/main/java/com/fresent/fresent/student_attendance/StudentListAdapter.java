package com.fresent.fresent.student_attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fresent.fresent.R;
import com.fresent.fresent.models.StudentEntity;

import java.util.List;

/**
 * Created by Aemiel on 31 Mar 2017.
 */

public class StudentListAdapter extends ArrayAdapter<StudentEntity> {
    private static final int LAYOUT = R.layout.student_list_item;
    private Context mContext;
    private List<StudentEntity> models;

    public StudentListAdapter(@NonNull Context context, @NonNull List<StudentEntity> objects) {
        super(context, LAYOUT, objects);
        this.mContext = context;
        this.models = objects;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        StudentEntity model = getItem(position);
        ViewHolder viewHolder;

        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(LAYOUT, parent, false);
            viewHolder.studentThumbnail =
                (ImageView) convertView.findViewById(R.id.studentThumbnail);
            viewHolder.studentName =
                (TextView) convertView.findViewById(R.id.studentCountTextView);
            viewHolder.attendanceStatus =
                (Spinner) convertView.findViewById(R.id.statusSpinner);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        String name = model.getFirstName() + " " + model.getLastName();
        viewHolder.studentName.setText(name);

        return convertView;
    }

    private class ViewHolder {
        ImageView studentThumbnail;
        TextView studentName;
        Spinner attendanceStatus;
    }
}
