package com.example.healthcareappdoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout IDNumInput, nameInput, PWInput, retypePWInput;
    private MaterialButton signUpBtn, toSignInBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        IDNumInput = findViewById(R.id.IDNumInput);
        nameInput = findViewById(R.id.nameInput);
        PWInput = findViewById(R.id.PWInput);
        retypePWInput = findViewById(R.id.retypePWInput);

        signUpBtn = findViewById(R.id.signUpButton);
        toSignInBtn = findViewById(R.id.toSignInButton);

        signUpBtn.setOnClickListener(signUpHandler);
        toSignInBtn.setOnClickListener(toSignInHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
    }

    private View.OnClickListener signUpHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createDoctor();
        }
    };

    private View.OnClickListener toSignInHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
    };

    private void createDoctor() {
        String IDNum, name, password, retypedPW;

        IDNum = IDNumInput.getEditText().getText().toString();
        name = nameInput.getEditText().getText().toString();
        password = PWInput.getEditText().getText().toString();
        retypedPW = retypePWInput.getEditText().getText().toString();

        if (IDNum.isEmpty()) {
            IDNumInput.setError("ID Number cannot be empty");
            IDNumInput.requestFocus();
        } else if (name.isEmpty()) {
            nameInput.setError("Name cannot be empty");
            nameInput.requestFocus();
        } else if (password.isEmpty()) {
            PWInput.setError("Password cannot be empty");
            PWInput.requestFocus();
        }

        if (password.equals(retypedPW) && !password.isEmpty()) {
            signUpBtn.setEnabled(false);

            mAuth.createUserWithEmailAndPassword(IDNum + "@healthcareappdoctor.com", password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    Uri builtURI = Uri.parse("http://192.168.1.4:8080/api/account/signup").buildUpon()
                                            .appendQueryParameter("idCard", IDNumInput.getEditText().getText().toString())
                                            .appendQueryParameter("name", nameInput.getEditText().getText().toString())
                                            .appendQueryParameter("password", PWInput.getEditText().getText().toString())
                                            .appendQueryParameter("role", "doctor")
                                            .build();

                                    URL obj = new URL(builtURI.toString());
                                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                    con.setRequestMethod("POST");
//
                                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                    String inputLine;
                                    String content = "";
                                    while ((inputLine = in.readLine()) != null) {
                                        content += inputLine;
                                    }
                                    if (content == "") {
                                        Log.d("SignUpAct", "No id card available");
                                    }
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                FirebaseUser user = mAuth.getCurrentUser();
                                String UID = user.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference("Doctors").child(UID);

                                HashMap hashMap = new HashMap<>();
                                hashMap.put("UID", UID);
                                hashMap.put("name", name);

                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                        }
                                    }
                                });
                            } else {
                                Log.d("SignUpActivity", task.getException().getMessage());
                            }
                        }
                    });
        } else {
            retypePWInput.setError("Retyped password must match password");
            retypePWInput.requestFocus();
        }
    }
}