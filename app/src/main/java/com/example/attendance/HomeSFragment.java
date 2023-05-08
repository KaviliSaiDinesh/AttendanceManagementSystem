package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeSFragment extends Fragment {

    TextView Slogout, Sname, SBuslocation, Sviewattendance, Sprofile;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_s, container, false);
        Slogout = (TextView) view.findViewById(R.id.Slogouttv);
        Sname = (TextView) view.findViewById(R.id.Sname);
        SBuslocation = (TextView) view.findViewById(R.id.Bustv);
        Sviewattendance = (TextView) view.findViewById(R.id.Sviewattendancetv);
        Sprofile = (TextView) view.findViewById(R.id.Sprofiletv);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference uc = FirebaseFirestore.getInstance().collection("users");
        DocumentReference df = uc.document(uid);

// Get the username field from the user's document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("Name");
                Sname.setText("Hello, " + username);
            }
        });

        SBuslocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DepInfo.class));
            }
        });

        Sviewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewAttendanceS.class));
            }
        });

        Sprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileSActivity.class));
            }
        });

        Slogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}