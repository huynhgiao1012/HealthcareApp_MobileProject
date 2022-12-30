package com.example.healthcareappdoctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Patient> patientArrayList;

    public PatientAdapter(Context context, ArrayList<Patient> patientArrayList) {
        this.context = context;
        this.patientArrayList = patientArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = patientArrayList.get(position);
        holder.patientPfp.setImageResource(R.mipmap.ic_launcher_round);
        holder.patientName.setText(patient.getName());
    }

    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView patientPfp;
        private final TextView patientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientPfp = itemView.findViewById(R.id.patientPfp);
            patientName = itemView.findViewById(R.id.patientName);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Patient patient = patientArrayList.get(getAdapterPosition());

            Intent intent = new Intent(context, MessageActivity.class);
            Bundle patientInfo = new Bundle();
            patientInfo.putString("name", patient.getName());
            patientInfo.putString("UID", patient.getUID());
            patientInfo.putString("token", patient.getToken());
            intent.putExtra("patientInfo", patientInfo);

            context.startActivity(intent);
        }
    }
}
