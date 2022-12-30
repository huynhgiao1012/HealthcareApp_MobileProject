package com.example.healthcareapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {
    private ImageView userPfp;
    private TextView userName, userPhone;
    private MaterialButton userLogoutBtn;
    private MaterialCardView userPersonalInfo, enterSymptoms, userAbout, userSignOut;
    private Intent intent;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        userPfp = view.findViewById(R.id.userPfp);
        userName = view.findViewById(R.id.userName);
        userPhone = view.findViewById(R.id.userPhone);
        userLogoutBtn = view.findViewById(R.id.userLogoutButton);
        userPersonalInfo = view.findViewById(R.id.userPersonalInfo);
        enterSymptoms = view.findViewById(R.id.enterSymptoms);
        userAbout = view.findViewById(R.id.userAbout);
        userSignOut = view.findViewById(R.id.userSignOut);

        userLogoutBtn.setOnClickListener(signOutHandler);

        userPersonalInfo.setOnClickListener(personalInfoHandler);
        enterSymptoms.setOnClickListener(enterSymptomsHandler);
        userAbout.setOnClickListener(aboutHandler);
        userSignOut.setOnClickListener(signOutHandler);
    }

    private View.OnClickListener personalInfoHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(getContext(), PersonalInfoActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    };

    private View.OnClickListener enterSymptomsHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(getContext(), EnterSymptomsActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    };

    private View.OnClickListener aboutHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(getContext(), AboutActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    };

    private View.OnClickListener signOutHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteFCMToken();
            mAuth.signOut();
            startActivity(new Intent(getContext(), SignInActivity.class));
        }
    };

    private void deleteFCMToken() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
        databaseReference.child("token").removeValue();
    }
}