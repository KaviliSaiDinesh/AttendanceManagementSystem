package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileTActivity extends AppCompatActivity {

    TextView Tname, Temail, Tid, Tdep;
    ImageView Tbackimg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tactivity);

        Tname = findViewById(R.id.Tnametv);
        Temail = findViewById(R.id.Temailtv);
        Tdep = findViewById(R.id.Tdeptv);
        Tid = findViewById(R.id.Tidtv);
        Tbackimg = findViewById(R.id.Tbackimg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Tbackimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTActivity.this, TeacherHome.class));
                finish();
            }
        });

        updateDetails();



    }

    public void updateDetails()
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference uc = FirebaseFirestore.getInstance().collection("teachers");
        DocumentReference df = uc.document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String dep = documentSnapshot.getString("department");
                String id = documentSnapshot.getString("id");

                Tname.setText(username);
                Tdep.setText(dep);
                Temail.setText(email);
                Tid.setText(id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileTActivity.this, "Teacher Details Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}