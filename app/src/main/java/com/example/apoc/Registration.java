package com.example.apoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apoc.DB.LogDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.Message;
import com.example.apoc.types.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private final String SUCCESS = "%s Succeeded!";
    private final String FAIL = "%s failed! Please try again later";
    private final String REG = "Register";
    private final String SIGN = "Sign in";
    private final String EMAIL = "Please enter email...";
    private final String PASS = "Please enter password!";
    private final String REGISTER_LOG = "%s has registered and now has a chance to SURVIVE!";
    private final String RESET_MSG = "Email was sent - you know what to do";
    private final String RESET_EMAIL_MSG = "Enter your email";

    public static final String RES_EMAIL = "email";
    public static final String RES_USER = "user";
    public static final String RES_IS_REGISTER = "is_reg";


    private EditText emailTV, passwordTV;
    private Button regBtn;
    private Button signInBtn;
    private ProgressBar progressBar;
    private String email, password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final Context context = this;

        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    registerNewUser();
                }
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    signInUser();
                }

            }
        });
        findViewById(R.id.register_pass_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTV.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(context, RESET_EMAIL_MSG, Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, RESET_MSG, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private boolean checkInput() {
        progressBar.setVisibility(View.VISIBLE);

        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), EMAIL, Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), PASS, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void registerNewUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), String.format(SUCCESS, REG), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            User user = new User(email);
                            (new UsersDB()).addItem(user);
                            (new LogDB()).addItem(new Message(String.format(REGISTER_LOG, user.getId()), user.getId()));
                            returnResult(true);
                        } else {
                            Toast.makeText(getApplicationContext(), String.format(FAIL, REG), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void signInUser() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), String.format(SUCCESS, SIGN), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            returnResult(false);
                        } else {
                            Toast.makeText(getApplicationContext(), String.format(FAIL, SIGN), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        regBtn = findViewById(R.id.register);
        signInBtn = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressBar);
    }

    private void returnResult(boolean isRegister) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RES_EMAIL, email);
        resultIntent.putExtra(RES_IS_REGISTER, isRegister);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}