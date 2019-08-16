package com.example.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout loginEmail;
    private TextInputLayout loginPassword;
    private Button loginAccountBtn, createAccountBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        //Layout
        loginEmail = findViewById(R.id.login_Email);
        loginPassword = findViewById(R.id.login_Password);
        loginAccountBtn = findViewById(R.id.login_signinBtn);
        createAccountBtn = findViewById(R.id.login_createBtn);

        mLoginProgress = new ProgressDialog(this);

        loginEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });
        loginAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEdittextEmail = loginEmail.getEditText().getText().toString();
                String loginEdittextPassword = loginPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(loginEdittextEmail) || !TextUtils.isEmpty(loginEdittextPassword)) {
                    mLoginProgress.setTitle("Logining user");
                    mLoginProgress.setMessage("Please wait while we check your account");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    sinnin(loginEdittextEmail, loginEdittextPassword);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });
    }

    private void sinnin(String email, String password) {
        //String email = loginEmail.getEditText().getText().toString();
        //String password = loginPassword.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mLoginProgress.dismiss();
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent loginIntent =
                                    new Intent(LoginActivity.this, MainActivity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            //finish();
                            //updateUI(user);
                        } else {
                            //If sign in fails, display a message to the user.
                            mLoginProgress.hide();
                            Log.w("Login", "signInWithEmail:failure",
                                    task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


    }

}
