package com.example.healthcareapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private ArrayList<Doctor> doctorList;
    private Context context;


    public DoctorAdapter(Context context, ArrayList<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor currentDoc = doctorList.get(position);
        holder.name.setText(currentDoc.getName());

        // TODO: Replace with another method to load image more efficiently
//        holder.pfp.setImageResource(currentDoc.getPfp());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final ImageView pfp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.doctorName);
            pfp = itemView.findViewById(R.id.pfp);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Doctor currentDoc = doctorList.get(getAdapterPosition());

            Intent intent = new Intent(context, MessageActivity.class);
            Bundle doctorInfo = new Bundle();
            doctorInfo.putString("name", currentDoc.getName());
            doctorInfo.putString("UID", currentDoc.getUID());
            doctorInfo.putString("token", currentDoc.getToken());
            intent.putExtra("doctorInfo", doctorInfo);

            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

    }
}
