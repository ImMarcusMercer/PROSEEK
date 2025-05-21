package com.example.proseekservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Handler;


public class SplashActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Handler handler;
    private FirebaseAuth mAuth;
    private Runnable timeoutRunnable;
    private boolean isNavigated = false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        handler = new Handler(Looper.getMainLooper());
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        timeoutRunnable = () -> {
            dismissDialogSafely();
            if (!isNavigated) {
                Toast.makeText(SplashActivity.this, "Operation timed out", Toast.LENGTH_SHORT).show();
                navigateTo(MainActivity.class);
            }
        };
        handler.postDelayed(timeoutRunnable, 5000);
        checkUserLoginStatus();
    }

    private void dismissDialogSafely() {
        handler.removeCallbacks(timeoutRunnable);
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException e) {
                Log.w("SplashActivity", "Dialog dismissal issue", e);
            }
        }
    }
    private void navigateTo(Class<?> targetActivity) {
        if (!isNavigated && !isFinishing() && !isDestroyed()) {
            isNavigated = true;
            dismissDialogSafely();
            startActivity(new Intent(this, targetActivity));
            finish();
        }
    }

    private void saveUserToPreferences(FirebaseUser user) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userId", user.getUid());
        editor.putString("email", user.getEmail());
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
    @Override
    protected void onDestroy() {
        dismissDialogSafely();
        super.onDestroy();
    }
    private void checkUserLoginStatus()
    {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            navigateTo(MainActivity.class);
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            navigateTo(MainActivity.class);
            return;
        }

        String userId = user.getUid();
        DatabaseReference typeRef = FirebaseDatabase
                .getInstance("https://appdev-69420-default-rtdb.firebaseio.com/").getReference("users").child(userId);

        typeRef.get().addOnCompleteListener(task -> {
            if (handler != null && timeoutRunnable != null) {
                handler.removeCallbacks(timeoutRunnable);
            }

            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    saveUserToPreferences(user);
                    startActivity(new Intent(this, MainActivityHomeScr.class));
                } else {
                    Log.w("FIREBASE", "User data not found.");
                    navigateTo(MainActivity.class);
                }
            } else {
                Log.e("FIREBASE", "Data fetch failed", task.getException());
                navigateTo(MainActivity.class);
            }
        });
    }

}
