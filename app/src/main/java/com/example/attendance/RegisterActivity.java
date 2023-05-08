package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Boolean valid;

    EditText regusername, regemail, regpass;
    EditText Sclass, Sregno, Ssection, Sdep;
    EditText Tclass, Tid, Tdep;
    CheckBox student, teacher;

    LinearLayout Studentll, Teacherll;


    Button register;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        regusername = findViewById(R.id.reg_username);
        regemail = findViewById(R.id.reg_email);
        regpass = findViewById(R.id.reg_password);
        student = findViewById(R.id.studentcheckbox);
        teacher = findViewById(R.id.teachercheckbox);

        register = findViewById(R.id.registerbtn);

        Sclass = findViewById(R.id.classedt);
        Tclass = findViewById(R.id.Tclassedt);
        Ssection = findViewById(R.id.sectionedt);
        Sdep = findViewById(R.id.RDepedt);
        Sregno = findViewById(R.id.RRegnoedt);
        Tdep = findViewById(R.id.TDepedt);
        Tid = findViewById(R.id.TIdedt);

        Studentll = findViewById(R.id.Student_layout);
        Teacherll = findViewById(R.id.teacher_layout);






        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    teacher.setChecked(false);
                    Studentll.setVisibility(View.VISIBLE);
                    Teacherll.setVisibility(View.GONE);



                }
            }
        });

        teacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    student.setChecked(false);
                    Studentll.setVisibility(View.GONE);
                    Teacherll.setVisibility(View.VISIBLE);

                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(regemail);
                checkField(regpass);
                checkField(regusername);


                //radiobutton validation

                if(!(student.isChecked() || teacher.isChecked())){
                    Toast.makeText(RegisterActivity.this, "Select the account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid)
                {
                    //Start the registration Process
                    fAuth.createUserWithEmailAndPassword(regemail.getText().toString(), regpass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("users").document(user.getUid());
                            DocumentReference tf = fStore.collection("teachers").document(user.getUid());
                            DocumentReference sf = fStore.collection("students").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("name", regusername.getText().toString());
                            userInfo.put("email", regemail.getText().toString());


                            //Specify if the user isadmin
                            if(teacher.isChecked()){
                                userInfo.put("isTeacher","1");
                                userInfo.put("department", Tdep.getText().toString());
                                userInfo.put("year", Tclass.getText().toString());
                                userInfo.put("id", Tid.getText().toString());
                                tf.set(userInfo);


                            }
                            if(student.isChecked()){
                                userInfo.put("isStudent","1");

                                userInfo.put("department", Sdep.getText().toString());
                                userInfo.put("year", Sclass.getText().toString());
                                userInfo.put("regno", Sregno.getText().toString());
                                userInfo.put("section", Ssection.getText().toString());
                                sf.set(userInfo);



                            }
                            df.set(userInfo);
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }
                    });

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


}
