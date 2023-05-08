package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherLogin extends AppCompatActivity {

    Boolean valid;
    Button TeacherLogin;

    EditText Temail, Tpass;

    TextView forgetpass;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TeacherLogin = findViewById(R.id.teacherloginbtn);
        Temail = findViewById(R.id.login_email);
        Tpass = findViewById(R.id.login_password);
        forgetpass = findViewById(R.id.forgetpass);

        TeacherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(Temail);
                checkField(Tpass);

                if(valid)
                {
                    fAuth.signInWithEmailAndPassword(Temail.getText().toString(), Tpass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(TeacherLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TeacherLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });





    }
    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("users").document(uid);
        //Extract the data from document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onsuccess"+ documentSnapshot.getData());
                //identify the user access level
                if(documentSnapshot.getString("isAdmin") != null)
                {
                    //user is admin
                    startActivity(new Intent(getApplicationContext(), AdminHome.class));
                    finish();
                }
                if(documentSnapshot.getString("isStudent") != null)
                {
                    startActivity(new Intent(getApplicationContext(), Studenthome.class));
                    finish();
                }
                if(documentSnapshot.getString("isTeacher") != null)
                {
                    startActivity(new Intent(getApplicationContext(), TeacherHome.class));
                    finish();
                }

            }
        });
    }

    public boolean checkField(EditText textField)
    {
        if(textField.getText().toString().isEmpty())
        {
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            DocumentReference df = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isTeacher")!=null){
                        startActivity(new Intent(getApplicationContext(), TeacherHome.class));
                        finish();
                    }
                    if(documentSnapshot.getString("isStudent")!=null){
                        startActivity(new Intent(getApplicationContext(), Studenthome.class));
                        finish();
                    }
                    if(documentSnapshot.getString("isAdmin")!=null){
                        startActivity(new Intent(getApplicationContext(), AdminHome.class));
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }
            });

        }
    }
}