package com.fresent.fresent.classes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fresent.fresent.R;
import com.fresent.fresent.models.Class;
import com.fresent.fresent.models.ClassEntity;

import java.util.List;

/**
 * Created by Aemiel on 31 Mar 2017.
 */

public class ClassListAdapter extends ArrayAdapter<ClassEntity> {

    private Context mContext;
    private List<ClassEntity> models;
    private static final int LAYOUT = R.layout.class_list_item_view;

    public ClassListAdapter(@NonNull Context context, @NonNull List<ClassEntity> objects) {
        super(context, LAYOUT, objects);
        this.mContext = context;
        this.models = objects;
    }

    private class ViewHolder {
        TextView courseAndSectionTextView;
        TextView studentCountTextView;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Class model = getItem(position);
        ViewHolder viewHolder;

        final View result;
        if( convertView == null ) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from( getContext() );
            convertView = inflater.inflate( LAYOUT, parent, false );
            viewHolder.courseAndSectionTextView =
                    (TextView) convertView.findViewById( R.id.courseAndSectionTextView );
            viewHolder.studentCountTextView =
                    (TextView) convertView.findViewById( R.id.studentCountTextView );
            result = convertView;
            convertView.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        String courseSectionText = model.getCourseCode() + " - " + model.getSection();
        int studentEnrolmentCount = model.getStudentEnrollments().toList().size();
        String studentCountText = studentEnrolmentCount + " Student"
                                    + (studentEnrolmentCount == 1?"":"s");
        viewHolder.courseAndSectionTextView.setText( courseSectionText );
        viewHolder.studentCountTextView.setText( studentCountText );

        return convertView;
    }

}
