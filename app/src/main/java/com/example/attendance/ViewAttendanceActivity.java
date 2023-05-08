package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewAttendanceActivity extends AppCompatActivity {
    private static final String TAG = "ViewAttendanceActivity";


    private ListView mListView;
    private TextView mDateEditText, total, present, absent, od;
    private Button mViewAttendanceButton;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String mSelectedDate;

    private boolean foundRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mListView = findViewById(R.id.list_view);
        mDateEditText = findViewById(R.id.date_edit_text);
        total = findViewById(R.id.TotalCounttv);
        present = findViewById(R.id.PresentCounttv);
        absent = findViewById(R.id.AbsentCounttv);
        od = findViewById(R.id.ODtv);
        mViewAttendanceButton = findViewById(R.id.view_attendance_button);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });


        mViewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedDate = mDateEditText.getText().toString().trim();
                if (!mSelectedDate.isEmpty()) {
                    retrieveAttendanceRecords();
                } else {
                    Toast.makeText(ViewAttendanceActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void retrieveAttendanceRecords() {
        final int t=0, p=0, o=0, a=0;
        final List<Attendance> attendanceList = new ArrayList<>();
        mFirestore.collection("students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot studentDocument : task.getResult()) {
                                String studentId = studentDocument.getId();
                                mFirestore.collection("students")
                                        .document(studentId)
                                        .collection("attendance")
                                        .document(mSelectedDate)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot attendanceDocument = task.getResult();
                                                    if (attendanceDocument.exists()) {
                                                        String attendanceStatus = attendanceDocument.getString("attendance");
                                                        Attendance attendance = new Attendance(studentDocument.getString("name"), attendanceStatus);
                                                        attendanceList.add(attendance);


                                                        AttendanceAdapter adapter = new AttendanceAdapter(ViewAttendanceActivity.this, attendanceList);
                                                        mListView.setAdapter(adapter);
                                                        updateAttendanceUI(attendanceList);

                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error getting attendance records: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting students: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Format the date as "YYYY-MM-DD"
                        String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth);
                        // Do something with the selected date (e.g. display it in a TextView)
                        mDateEditText.setText(date);
                    }
                },
                year, month, dayOfMonth);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
    private void updateAttendanceUI(List<Attendance> attendanceList) {
        AttendanceAdapter adapter = new AttendanceAdapter(ViewAttendanceActivity.this, attendanceList);
        mListView.setAdapter(adapter);

        int presentCount = 0;
        int absentCount = 0;
        int odCount = 0;
        int totalCount = attendanceList.size();

        for (Attendance attendance : attendanceList) {
            if (attendance.getAttendanceStatus().equals("Present")) {
                presentCount++;
            } else if (attendance.getAttendanceStatus().equals("Absent")) {
                absentCount++;
            } else if (attendance.getAttendanceStatus().equals("OD")) {
                odCount++;
            }
        }
        present.setText(String.valueOf(presentCount));
        absent.setText(String.valueOf(absentCount));
        od.setText(String.valueOf(odCount));
        total.setText(String.valueOf(totalCount));

    }
}

