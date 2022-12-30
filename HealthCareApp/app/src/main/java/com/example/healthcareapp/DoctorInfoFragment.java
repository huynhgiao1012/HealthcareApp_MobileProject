package com.example.healthcareapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;


public class DoctorInfoFragment extends BottomSheetDialogFragment {
    private AppBarLayout appBarLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDragHandleView dragHandleView;
    private TextView doctorNameTextView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.doctorInfoAppBarLayout);
        dragHandleView = view.findViewById(R.id.doctorInfoDragHandle);
        doctorNameTextView = view.findViewById(R.id.doctorInfoName);

        String doctorName = getArguments().getString("doctorName");
        doctorNameTextView.setText(doctorName);

        toolbar = view.findViewById(R.id.doctorInfoToolbar);
        toolbar.setNavigationOnClickListener(navOnClickHandler);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetBehavior = dialog.getBehavior();
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback);
        return dialog;
    }

    private void showToolbar() {
        appBarLayout.setVisibility(View.VISIBLE);
        dragHandleView.setVisibility(View.GONE);
    }

    private void hideToolbar() {
        appBarLayout.setVisibility(View.GONE);
        dragHandleView.setVisibility(View.VISIBLE);
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                showToolbar();
            } else {
                hideToolbar();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    private View.OnClickListener navOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    };
}