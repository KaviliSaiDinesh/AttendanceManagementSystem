package com.example.attendance;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeTFragment extends Fragment {

    TextView Tlogout, Tname, Ttakeattendance, Tviewattendance, Tprofile;

    ImageView take, viewattendance, profile, logout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_t, container, false);
        Tlogout = (TextView) view.findViewById(R.id.Tlogouttv);
        Tname = (TextView) view.findViewById(R.id.Tname);
        Ttakeattendance = (TextView) view.findViewById(R.id.takeattendancetv);
        Tviewattendance = (TextView) view.findViewById(R.id.viewattendancetv);
        Tprofile = (TextView) view.findViewById(R.id.profiletv);

        take = (ImageView) view.findViewById(R.id.Ttakeattendanceimg);
        viewattendance = (ImageView) view.findViewById(R.id.TviewAttendanceimg);
        profile = (ImageView) view.findViewById(R.id.Tprofileimg);
        logout = (ImageView) view.findViewById(R.id.Tlogoutimg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference uc = FirebaseFirestore.getInstance().collection("teachers");
        DocumentReference df = uc.document(uid);

// Get the username field from the user's document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("name");
                Tname.setText("Hello, " + username);
            }
        });

        Ttakeattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TakeAttedance.class));
            }
        });
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TakeAttedance.class));
            }
        });

        Tviewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewAttendanceActivity.class));
            }
        });
        viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewAttendanceActivity.class));
            }
        });

        Tprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileTActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileTActivity.class));
            }
        });

        Tlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
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