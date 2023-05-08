package com.example.attendance;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationTFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_t, container, false);

        recyclerView = view.findViewById(R.id.recycler_viewT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("teachers").document(uid).collection("notifications")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("NotificationFragment", "Listen failed.", e);
                            return;
                        }
                        notificationList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String title = documentSnapshot.getString("title");
                            String message = documentSnapshot.getString("message");
                            String timestamp = documentSnapshot.getString("date-time");
                            NotificationModel notification = new NotificationModel(title, message, timestamp);
                            notificationList.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        return view;
    }
}