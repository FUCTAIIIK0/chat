package com.example.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Firebase Auth


        //Layout
        mToolbar = findViewById(R.id.mainPageToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Chat Main Page");

        //Layout





    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser curentUser = mAuth.getCurrentUser();
        if (curentUser == null){
            sendToStart();
        }
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.main_menu, menu);



         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId() == R.id.mainLogoutBtn){
             Log.d("Menu", "Logout");
             if (FirebaseAuth.getInstance().getCurrentUser() != null){
                 FirebaseAuth.getInstance().signOut();
                 sendToStart();
                 Log.d("Menu", "Logout null");
             }

         }
         if (item.getItemId() == R.id.mainAllUsersBtn){
             //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             //Log.d("Menu", "All users "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
         }
         if (item.getItemId() == R.id.mainSettingsBtn){

         }


         return true;
    }
}
