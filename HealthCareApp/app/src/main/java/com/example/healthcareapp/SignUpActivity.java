package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthcareapp.utilities.Constants;
import com.example.healthcareapp.utilities.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout IDNumInput, nameInput, PWInput, retypePWInput;
    private Button signUpBtn, toSignInPgBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        IDNumInput = findViewById(R.id.IDNumField);
        nameInput = findViewById(R.id.nameField);
        PWInput = findViewById(R.id.passwordField);
        retypePWInput = findViewById(R.id.retypePWField);

        db = FirebaseDatabase.getInstance().getReference();

        signUpBtn = findViewById(R.id.signUpButton);
        toSignInPgBtn = findViewById(R.id.alreadyHadAccntBtn);

        signUpBtn.setOnClickListener(signUpHandler);

        toSignInPgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toIntent(SignInActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            toIntent(SignInActivity.class);
        }
    }

    private void toIntent(Class destinationClass) {
        Intent intent = new Intent(getApplicationContext(), destinationClass);
        startActivity(intent);
    }

    private View.OnClickListener signUpHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createUser();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    };

    private void createUser() {
        String IDNum = IDNumInput.getEditText().getText().toString();
        String name = nameInput.getEditText().getText().toString();
        String password = PWInput.getEditText().getText().toString();
        String retypedPW = retypePWInput.getEditText().getText().toString();

        String hashedPW;

        if (IDNum.isEmpty()) {
            IDNumInput.setError(Utilities.setEmptyErrorText("ID number"));
            IDNumInput.requestFocus();
        }
        else if (name.isEmpty()) {
            nameInput.setError(Utilities.setEmptyErrorText("Name"));
            nameInput.requestFocus();
        }
        else if (password.isEmpty()) {
            PWInput.setError(Utilities.setEmptyErrorText("Password"));
            PWInput.requestFocus();
        }

        if (password.equals(retypedPW) && !password.isEmpty() && !retypedPW.isEmpty()) {
            signUpBtn.setEnabled(false);

            try {
                hashedPW = Utilities.hashPassword(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            mAuth.createUserWithEmailAndPassword(IDNum + "@healthcareapp.com", password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // TODO: Add logics to add user to db
                        try {
                            Uri builtURI = Uri.parse("http://" + Constants.IP_ADDRESS + ":8080/api/account/signup").buildUpon()
                                    .appendQueryParameter("idCard", IDNumInput.getEditText().getText().toString())
                                    .appendQueryParameter("name", nameInput.getEditText().getText().toString())
                                    .appendQueryParameter("password", PWInput.getEditText().getText().toString())
                                    .appendQueryParameter("role", "patient")
                                    .build();

                            URL obj = new URL(builtURI.toString());
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestMethod("POST");

                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            String content = "";
                            while ((inputLine = in.readLine()) != null) {
                                content += inputLine;
                            }
                            if (content == "") {
                                Log.d("SignUpAct", "No id card available");
                            }
                            Constants.idCard = IDNum;
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        FirebaseUser user = mAuth.getCurrentUser();
                        String UID = user.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UID);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("UID", UID);
                        hashMap.put("name", name);

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    toIntent(SignInActivity.class);
                                }
                            }
                        });
                    }
                    else {
                        Log.d("SignUpActivity", task.getException().getMessage());
                    }
                }
            });
        }
        else {
            retypePWInput.setError("Retyped password must match password");
            retypePWInput.requestFocus();
        }
    }
}