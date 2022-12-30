package com.example.healthcareapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcareapp.utilities.Constants;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PersonalInfoActivity extends AppCompatActivity {
    private TextInputEditText userName, userPhone, userNewPW;
    private ImageView userPfp;
    private MaterialButton updateInfoBtn;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userNewPW = findViewById(R.id.userNewPassword);
        userPfp = findViewById(R.id.userPfp);
        updateInfoBtn = findViewById(R.id.updateInfoButton);
        toolbar = findViewById(R.id.toolbar);

        updateInfoBtn.setOnClickListener(updateInfoHandler);

        toolbar.setNavigationOnClickListener(toolbarOnBackPressedHandler);
    }

    private View.OnClickListener updateInfoHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Add logics to handle checking if info is new and saving to DB
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateInfo(userName.getText().toString(), userNewPW.getText().toString());
                }
            });
            thread.start();
        }
    };

    private void updateInfo(String name, String password) {
        try {
            Uri builtURI = Uri.parse("http://" + Constants.IP_ADDRESS + ":8080/api/account/updateInfo").buildUpon()
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("idCard", Constants.idCard)
                    .appendQueryParameter("password", password)
                    .build();

            URL obj = new URL(builtURI.toString());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String content = "";
            while ((inputLine = in.readLine()) != null) {
                content += inputLine;
            }
            if (content == "") {
                Log.d("SaveSymptomAct", "No id card available");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener toolbarOnBackPressedHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
        }
    };
}