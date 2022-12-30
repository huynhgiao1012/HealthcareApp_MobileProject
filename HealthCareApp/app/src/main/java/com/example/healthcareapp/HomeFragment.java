package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView topNewsRV;
    private ArrayList<NewsArticle> topNewsList;
    private TopNewsAdapter adapter;
    private MaterialCardView toUserInfo, toAddSymptoms, toCallDoctor;
    private NavigationBarView parentBottomNav;
    private TextView patientNameTextView;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        topNewsRV = getView().findViewById(R.id.topNewsRV);
        topNewsRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        topNewsList = new ArrayList<>();
        adapter = new TopNewsAdapter(getContext(), topNewsList);
        topNewsRV.setAdapter(adapter);

        toUserInfo = view.findViewById(R.id.toUserInfoCard);
        toAddSymptoms = view.findViewById(R.id.toAddSymptomsCard);
        toCallDoctor = view.findViewById(R.id.toCallDoctorCard);

        toUserInfo.setOnClickListener(toUserInfoHandler);
        toAddSymptoms.setOnClickListener(toAddSymptomsHandler);
        toCallDoctor.setOnClickListener(toCallDoctorHandler);

        parentBottomNav = view.getRootView().findViewById(R.id.bottomNav);

        patientNameTextView = view.findViewById(R.id.patientName);

        String UID = mAuth.getUid();
        if (UID != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
            databaseReference.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        String patientName = task.getResult().getValue().toString();
                        patientNameTextView.setText(patientName);
                    }
                    else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    populateData();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }

    private void populateData() throws IOException, JSONException {
        String GET_URL = "http://" + Constants.IP_ADDRESS + ":8080/api/news/getNewsFromApi";
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String content = "";
        while ((inputLine = in.readLine()) != null) {
            content += inputLine;
        }
        Log.d("News", content);
        JSONArray newsList = new JSONArray(content);

        for (int i = 0; i < newsList.length(); i++) {
            JSONObject news = newsList.getJSONObject(i);

            topNewsList.add(new NewsArticle(
                    news.getString("title"),
                    news.getString("description"),
                    0));
        }
        in.close();
    }

    private View.OnClickListener toUserInfoHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), PersonalInfoActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    };

    private View.OnClickListener toAddSymptomsHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), EnterSymptomsActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    };

    private View.OnClickListener toCallDoctorHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            parentBottomNav.setSelectedItemId(R.id.chatItem);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentContainer, new ChatFragment());
            transaction.commit();
        }
    };
}