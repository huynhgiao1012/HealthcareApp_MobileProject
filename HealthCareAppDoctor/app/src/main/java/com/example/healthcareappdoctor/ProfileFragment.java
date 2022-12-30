package com.example.healthcareappdoctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private MaterialButton signOutBtn;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        signOutBtn = view.findViewById(R.id.signOutButton);
        signOutBtn.setOnClickListener(signOutHandler);
    }

    private View.OnClickListener signOutHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteFCMToken();
            mAuth.signOut();
            startActivity(new Intent(getContext(), SignInActivity.class));
        }
    };

    private void deleteFCMToken() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doctors").child(mAuth.getUid());
        databaseReference.child("token").removeValue();
    }
}