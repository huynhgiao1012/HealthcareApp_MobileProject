package com.example.healthcareapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {
    private ArrayList<Symptom> symptomList;
    private Context context;

    public SymptomAdapter(Context context, ArrayList<Symptom> symptomList) {
        this.context = context;
        this.symptomList = symptomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Symptom currentSymptom = symptomList.get(position);

        holder.symptomName.setText(currentSymptom.getName());
        holder.symptomDescription.setText(currentSymptom.getDescription());
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView symptomName, symptomDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            symptomName = itemView.findViewById(R.id.symptomName);
            symptomDescription = itemView.findViewById(R.id.symptomDescription);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            if (symptomDescription.getMaxLines() < 100) {
                symptomDescription.setMaxLines(100);
            }
            else {
                symptomDescription.setMaxLines(2);
            }
        }
    }
}
