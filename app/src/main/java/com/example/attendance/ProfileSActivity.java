package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSActivity extends AppCompatActivity {

    TextView Sname, Semail, Sregno, Sdep, Ssec;
    ImageView Sbackimg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_s);

        Sname = findViewById(R.id.Snametv);
        Semail = findViewById(R.id.Semailtv);
        Sdep = findViewById(R.id.Sdeptv);
        Sregno = findViewById(R.id.Sregisternotv);
        Ssec = findViewById(R.id.Ssectiontv);
        Sbackimg = findViewById(R.id.Sbackimg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Sbackimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSActivity.this, Studenthome.class));
                finish();
            }
        });

        updateDetails();



    }

    public void updateDetails()
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference uc = FirebaseFirestore.getInstance().collection("students");
        DocumentReference df = uc.document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String dep = documentSnapshot.getString("department");
                String regno = documentSnapshot.getString("regno");
                String sec = documentSnapshot.getString("section");

                Sname.setText(username);
                Sdep.setText(dep);
                Semail.setText(email);
                Sregno.setText(regno);
                Ssec.setText(sec);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSActivity.this, "Student Details Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}