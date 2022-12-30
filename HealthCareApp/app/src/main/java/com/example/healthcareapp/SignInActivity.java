package com.example.healthcareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcareapp.utilities.Constants;
import com.example.healthcareapp.utilities.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {
    private TextInputLayout usernameInput, passwordInput;
    private CheckBox rememberMeCB;
    private Button signInBtn, toSignUpBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        usernameInput = findViewById(R.id.IDNumField);
        passwordInput = findViewById(R.id.passwordField);
        signInBtn = findViewById(R.id.signInButton);
        toSignUpBtn = findViewById(R.id.toSignUpPageBtn);

        signInBtn.setOnClickListener(signInHandler);
        toSignUpBtn.setOnClickListener(toSignUpHandler);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private View.OnClickListener signInHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signInUser();
        }
    };

    private View.OnClickListener onBackPressedHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener toSignUpHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    };

    private void signInUser() {
        String IDNum = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();

        if (IDNum.isEmpty()) {
            usernameInput.setError(Utilities.setEmptyErrorText("ID number"));
            usernameInput.requestFocus();
        } else if (password.isEmpty()) {
            passwordInput.setError(Utilities.setEmptyErrorText("Password"));
            passwordInput.requestFocus();
        } else {
            signInBtn.setEnabled(false);

            mAuth.signInWithEmailAndPassword(IDNum + "@healthcareapp.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    Uri builtURI = Uri.parse("http://" + Constants.IP_ADDRESS + ":8080/api/account/getAccountByIdCard").buildUpon()
                                            .appendQueryParameter("idCard", IDNum)
                                            .appendQueryParameter("password", passwordInput.getEditText().getText().toString())
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
                                        Log.d("SignInAct", "No id card available");
                                    }
                                    Constants.idCard = IDNum;
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        thread.start();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}