package com.mahmudz.firebaseauthdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText inputLoginEmail, inputLoginPassword;
    private Button loginBTN, registerPageBTN;
    private ProgressDialog progressDialog;
    private Intent dashboardPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        dashboardPage = new Intent(getApplicationContext(), DashboardActivity.class);
        loginBTN = findViewById(R.id.loginBTN);
        registerPageBTN = findViewById(R.id.registerPageBTN);

        inputLoginEmail = findViewById(R.id.inputLoginEmail);
        inputLoginPassword = findViewById(R.id.inputLoginPassword);

        registerPageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerPage = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerPage);
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputLoginEmail.getText().toString().trim();
                String password  = inputLoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputLoginEmail.setError("Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputLoginPassword.setError("Required.");
                    return;
                }

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("AuthLog", "signInWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    startActivity(dashboardPage);
                                } else {
                                    Log.w("AuthLog", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(dashboardPage);
        }
    }

}
