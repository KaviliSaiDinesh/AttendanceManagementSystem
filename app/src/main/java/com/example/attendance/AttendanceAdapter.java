package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class AttendanceAdapter extends ArrayAdapter<Attendance> {

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        super(context, 0, attendanceList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_attendance, parent, false);
        }

        Attendance attendance = getItem(position);

        TextView studentNameTextView = view.findViewById(R.id.student_name_text_view);
        studentNameTextView.setText(attendance.getStudentName());

        TextView attendanceStatusTextView = view.findViewById(R.id.attendance_status_text_view);
        attendanceStatusTextView.setText(attendance.getAttendanceStatus());

        return view;
    }
}
