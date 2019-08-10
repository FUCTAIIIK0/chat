package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Layout
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    //Firebase Auth
    private Button createAccountBtn;
    private TextInputLayout regDisplayName,regEmail,regPassword;
    private Toolbar mToolbar;
    //ProgressDialog
    private ProgressDialog mRegProgress;
    //Realtime database
    private DatabaseReference mDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        //Layout
            //Progress dialog
        mRegProgress = new ProgressDialog(this);
            //Toolbar set
        mToolbar = findViewById(R.id.registerToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regDisplayName = findViewById(R.id.regDisplayName);
        regEmail = findViewById(R.id.regEmailEdittext);
        regPassword = findViewById(R.id.regPassword);

        createAccountBtn = findViewById(R.id.createBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String displayName = regDisplayName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String pasword = regPassword.getEditText().getText().toString();
                if (pasword.length()<5){
                    Toast.makeText(RegisterActivity.this, "Password must be more than 6 characters",
                            Toast.LENGTH_SHORT).show();
                }else {

                if (!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(email)|| !TextUtils.isEmpty(pasword)){
                    mRegProgress.setTitle("Registring User");
                    mRegProgress.setMessage("Please wait while we create account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    registerUser(displayName,email,pasword);
                }
            }}
        });

    }

    private void registerUser(final String displayName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser curent_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = curent_user.getUid();

                            mDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", displayName);
                            userMap.put("status","Hi there ");
                            userMap.put("online","default");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");

                            mDatabse.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mRegProgress.dismiss();

                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Auth", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                        updateUI(user);
                                    }
                                }
                            });
                    } else {
                            mRegProgress.hide();
                            // If sign in fails, display a message to the user.
                            Log.w("Auth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Cannot Sing in. Please check the form and try again.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        //this.user = user;

        return;
    }
}
