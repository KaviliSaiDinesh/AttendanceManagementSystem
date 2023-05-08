package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminNotificationActivity extends AppCompatActivity {

    private EditText etTitle, etMessage, etRegNo;
    private Button btnSendNotification;
    private String type, formattedDate;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        etTitle = findViewById(R.id.et_title);
        etMessage = findViewById(R.id.et_message);
        etRegNo = findViewById(R.id.et_regno);
        btnSendNotification = findViewById(R.id.btn_send_notification);
        RadioGroup typeRadioGroup = findViewById(R.id.ST_radio_group);
        typeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    RadioButton selectedRadioButton = findViewById(i);
                    type = selectedRadioButton.getText().toString();
        });
        long timestamp = System.currentTimeMillis(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = dateFormat.format(new Date(timestamp));


        db = FirebaseFirestore.getInstance();

        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Student")){
                    sendNotificationStudent();
                } else if (type.equals("Teacher")) {
                    sendNotificationTeacher();
                }
            }
        });
    }

    private void sendNotificationStudent() {
        final String title = etTitle.getText().toString();
        final String message = etMessage.getText().toString();
        final String regNo = etRegNo.getText().toString();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Please enter notification title");
            return;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Please enter notification message");
            return;
        }

        if (TextUtils.isEmpty(regNo)) {
            etRegNo.setError("Please enter student register number");
            return;
        }

        db.collection("students")
                .whereEqualTo("regno", regNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(AdminNotificationActivity.this, "No student found with register number " + regNo, Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String uid = documentSnapshot.getId();
                                Map<String, Object> data = new HashMap<>();
                                data.put("title", title);
                                data.put("message", message);
                                data.put("date-time", formattedDate);
                                db.collection("students")
                                        .document(uid)
                                        .collection("notifications")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(AdminNotificationActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                                                etTitle.setText("");
                                                etMessage.setText("");
                                                etRegNo.setText("");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminNotificationActivity.this, "Failed to send notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminNotificationActivity.this, "Failed to get student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendNotificationTeacher() {
        final String title = etTitle.getText().toString();
        final String message = etMessage.getText().toString();
        final String regNo = etRegNo.getText().toString();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Please enter notification title");
            return;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Please enter notification message");
            return;
        }

        if (TextUtils.isEmpty(regNo)) {
            etRegNo.setError("Please enter Teacher Name");
            return;
        }

        db.collection("teachers")
                .whereEqualTo("name", regNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(AdminNotificationActivity.this, "No teacher found with given name " + regNo, Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String uid = documentSnapshot.getId();
                                Map<String, Object> data = new HashMap<>();
                                data.put("title", title);
                                data.put("message", message);
                                data.put("date-time", formattedDate);
                                db.collection("teachers")
                                        .document(uid)
                                        .collection("notifications")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(AdminNotificationActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                                                etTitle.setText("");
                                                etMessage.setText("");
                                                etRegNo.setText("");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminNotificationActivity.this, "Failed to send notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminNotificationActivity.this, "Failed to get teacher: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


