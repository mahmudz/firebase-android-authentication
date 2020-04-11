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

public class RegisterActivity extends AppCompatActivity {

    private EditText inputRegisterEmail, inputRegisterPassword;
    private Button registerBTN, loginPageBTN;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Intent loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        registerBTN = findViewById(R.id.registerBTN);
        loginPageBTN = findViewById(R.id.loginPageBTN);

        inputRegisterEmail = findViewById(R.id.inputRegisterEmail);
        inputRegisterPassword = findViewById(R.id.inputRegisterPassword);
        loginPage = new Intent(getApplicationContext(), LoginActivity.class);

        loginPageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(loginPage);
            }
        });


        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputRegisterEmail.getText().toString().trim();
                String password  = inputRegisterPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputRegisterEmail.setError("Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputRegisterPassword.setError("Required.");
                    return;
                }


                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("AuthLog", "createUserWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                     startActivity(loginPage);
                                } else {
                                    Log.w("AuthLog", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
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
            Intent dashboardPage = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(dashboardPage);
        }
    }
}
