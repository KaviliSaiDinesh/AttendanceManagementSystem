package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class TakeAttedance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private CollectionReference studentsCollection;
    private static final String TAG = "TakeAttedance";
    private List<DocumentSnapshot> students;
    private Map<String, String> attendanceMap;

    private LinearLayout studentListLayout;
    private Button submitAttendanceButton, viewStudentsButton;

   Spinner yearspin, secspin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attedance);

        db = FirebaseFirestore.getInstance();
        studentsCollection = db.collection("students");
        attendanceMap = new HashMap<>();

        studentListLayout = findViewById(R.id.student_attendance_layout);
        yearspin = findViewById(R.id.Tdepspinner);
        secspin = findViewById(R.id.Tclassspinner);
        viewStudentsButton = findViewById(R.id.viewstudentsbtn);

        submitAttendanceButton = findViewById(R.id.submit_attendance_button);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspin.setAdapter(adapter1);
        yearspin.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sections, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secspin.setAdapter(adapter2);
        secspin.setOnItemSelectedListener(this);

        viewStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentListLayout.removeAllViews();
                String stdyear = yearspin.getSelectedItem().toString();
                String stdsec = secspin.getSelectedItem().toString();


                studentsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    students = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot student : students) {

                        if(student.getString("year").equals(stdyear) && student.getString("section").equals(stdsec)) {
                            String studentName = student.getString("name");
                            String regno = student.getString("regno");


                            // Inflate the layout for the student
                            LinearLayout studentLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.student_attendance_layout, null);

                            // Set the tag for the EditText
                            EditText reasonEditText = studentLayout.findViewById(R.id.absentReasonedt);
                            reasonEditText.setTag(student.getId() + "_reason");

                            // Set the student's name
                            TextView studentNameTextView = studentLayout.findViewById(R.id.student_name);
                            studentNameTextView.setText(studentName);

                            // Set the student's regno
                            TextView studentRegnoTextView = studentLayout.findViewById(R.id.student_regno);
                            studentRegnoTextView.setText(regno);


                            // Add a listener to the radio group to update the attendance map
                            RadioGroup attendanceRadioGroup = studentLayout.findViewById(R.id.attendance_radio_group);

                            // Set the default value of the radio group to "Present"
                            RadioButton present = studentLayout.findViewById(R.id.present_radio_button);
                            present.setChecked(true);

                            attendanceRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                                RadioButton selectedRadioButton = findViewById(i);
                                String attendance = selectedRadioButton.getText().toString();
                                // Show the reason EditText field only when "OD" or "Absent" is selected

                                if (attendance.equals("OD") || attendance.equals("Absent")) {
                                    reasonEditText.setVisibility(View.VISIBLE);
                                } else {
                                    reasonEditText.setVisibility(View.GONE);
                                }

                                attendanceMap.put(student.getId(), attendance);
                            });

                            // Add a check to ensure that the attendance map is updated with the default value if no radio button is selected by the user
                            if (attendanceRadioGroup.getCheckedRadioButtonId() == -1) {
                                attendanceMap.put(student.getId(), "Present");
                            }
                            // Add the student layout to the list layout
                            studentListLayout.addView(studentLayout);
                        }
                    }

                    if (studentListLayout.getChildCount() == 0) {
                        TextView noStudentsTextView = new TextView(TakeAttedance.this);
                        noStudentsTextView.setText("No students found");
                        studentListLayout.addView(noStudentsTextView);
                    }
                });


            }
        });

        // Add a listener to the submit button to update the attendance in Firestore
        submitAttendanceButton.setOnClickListener(view -> {
            for (DocumentSnapshot student : students) {
                String studentId = student.getId();
                String attendance = attendanceMap.get(studentId);

                // Get the reason from the EditText field when "OD" or "Absent" is selected
                String reason = "";
                if (attendance != null && (attendance.equals("OD") || attendance.equals("Absent"))) {
                    EditText reasonEditText = studentListLayout.findViewWithTag(studentId + "_reason");
                    reason = reasonEditText.getText().toString();
                }

                if (attendance != null) {
                    Map<String, Object> attendanceData = new HashMap<>();
                    attendanceData.put("attendance", attendance);
                    attendanceData.put("reason", reason); // Add the reason to the attendanceData map

                    db.collection("students").document(studentId)
                            .collection("attendance").document(getTodayDate())
                            .set(attendanceData, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Attendance for " + student.getString("name") + " updated");
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error updating attendance for " + student.getString("name"), e);
                            });
                } else if(attendance == null) {
                    Map<String, Object> attendanceData = new HashMap<>();
                    attendanceData.put("attendance", "Present");

                    db.collection("students").document(studentId)
                            .collection("attendance").document(getTodayDate())
                            .set(attendanceData, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Attendance for " + student.getString("name") + " updated");
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error updating attendance for " + student.getString("name"), e);
                            });
                }
            }

            Toast.makeText(this, "Attendance updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), TeacherHome.class));
            finish();
        });

    }

    private String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
