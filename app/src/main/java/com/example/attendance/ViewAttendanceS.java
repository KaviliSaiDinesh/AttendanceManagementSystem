package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewAttendanceS extends AppCompatActivity {
    private static final String TAG = "ViewAttendanceActivity";
    private TextView mDateEditText, nametv, statustv, reasontv;
    private Button mViewAttendanceButton;

    private LinearLayout reasonll;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String mSelectedDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_s);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        nametv = findViewById(R.id.StudentNametv);
        statustv = findViewById(R.id.Statustv);
        mDateEditText = findViewById(R.id.date_editS_text);
        reasontv = findViewById(R.id.Reasonshowtv);
        mViewAttendanceButton = findViewById(R.id.Sview_attendance_button);
        reasonll = findViewById(R.id.SReasonll);
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
                    Toast.makeText(ViewAttendanceS.this, "Please select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void retrieveAttendanceRecords() {
        String studentId = mCurrentUser.getUid();
        DocumentReference df = mFirestore.collection("students").document(studentId);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("name");
                nametv.setText(name);
            }
        });
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
                                String status = attendanceDocument.getString("attendance");
                                statustv.setText(status);
                                if(status.equals("Absent") && status.equals("OD"))
                                {
                                    reasonll.setVisibility(View.VISIBLE);
                                    String reason = attendanceDocument.getString("reason");
                                    reasontv.setText(reason);
                                }else if(status.equals("Present"))
                                    reasonll.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting attendance records: " + task.getException(), Toast.LENGTH_SHORT).show();
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
}