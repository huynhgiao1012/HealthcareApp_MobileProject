package com.example.healthcareappdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout IDNumInput, PWInput;
    private MaterialButton signInBtn, toSignUpBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        IDNumInput = findViewById(R.id.IDNumInput);
        PWInput = findViewById(R.id.PWInput);

        signInBtn = findViewById(R.id.signInButton);
        toSignUpBtn = findViewById(R.id.toSignUpButton);

        signInBtn.setOnClickListener(signInHandler);
        toSignUpBtn.setOnClickListener(toSignUpHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private View.OnClickListener signInHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signInUser();
        }
    };

    private View.OnClickListener toSignUpHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    };

    private void signInUser() {
        String IDNum, password;
        IDNum = IDNumInput.getEditText().getText().toString();
        password = PWInput.getEditText().getText().toString();

        if (IDNum.isEmpty()) {
            IDNumInput.setError("ID number cannot be empty");
            IDNumInput.requestFocus();
        }
        else if (password.isEmpty()) {
            PWInput.setError("Password cannot be empty");
            PWInput.requestFocus();
        }
        else {
            signInBtn.setEnabled(false);

            mAuth.signInWithEmailAndPassword(IDNum + "@healthcareappdoctor.com", password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                signInBtn.setEnabled(true);
                            }
                        }
                    });
        }
    }
}