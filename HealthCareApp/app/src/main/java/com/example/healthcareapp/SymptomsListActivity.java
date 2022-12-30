package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class SymptomsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Symptom> symptomsList;
    private SymptomAdapter adapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_list);

        recyclerView = findViewById(R.id.symptomsListRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        symptomsList = new ArrayList<>();
        adapter = new SymptomAdapter(getApplicationContext(), symptomsList);
        recyclerView.setAdapter(adapter);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(onBackPressedHandler);

        populateData();
    }

    private void populateData() {
        // TODO: Add logics to pull symptoms from DB
        symptomsList.add(new Symptom("Symptom Test", getString(R.string.lorem_ipsum_text)));
        symptomsList.add(new Symptom("Symptom Test 2", getString(R.string.lorem_ipsum_text)));
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onBackPressedHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    };
}