package com.example.proseekservices;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.common.value.qual.StringVal;

public class MainActivityProfile extends AppCompatActivity {

    TextView username;
    TextView email;
    TextView location;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Logout
        LinearLayout loggingout = findViewById(R.id.loggingout);
        loggingout.setOnClickListener(task-> {
            LogoutManager.logout(this);
        });


        //Edit button
        TextView editProfile = findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(task-> {
            Intent intent = new Intent(this, MainActivityEditProf.class);
            startActivity(intent);
        });
        ImageView editProfile1 = findViewById(R.id.edit_prof);
        editProfile1.setOnClickListener(task-> {
            Intent intent = new Intent(this, MainActivityEditProf.class);
            startActivity(intent);
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        user.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullName = snapshot.child("username").getValue(String.class);
                    String userEmail = snapshot.child("email").getValue(String.class);
                    String userLocation = snapshot.child("location").getValue(String.class);
                    String userNumber = snapshot.child("number").getValue(String.class);
                    Long ratingLong = snapshot.child("rating").getValue(Long.class);


                    StringBuilder servicesBuilder = new StringBuilder();
                    for (com.google.firebase.database.DataSnapshot serviceSnap : snapshot.child("services").getChildren()) {
                        String service = serviceSnap.getValue(String.class);
                        if (service != null) {
                            if (servicesBuilder.length() > 0) servicesBuilder.append(", ");
                            servicesBuilder.append(service);
                        }
                    }
                    username = findViewById(R.id.proper_username);
                    email = findViewById(R.id.email);
                    location = findViewById(R.id.location);
                    number = findViewById(R.id.number);

                    RatingBar ratingBar = findViewById(R.id.profile_rating1);
                    if (ratingLong != null) {
                        float rating = ratingLong.floatValue();
                        ratingBar.setRating(rating);
                    }

                    // Set to views
                    username.setText(fullName);
                    email.setText(userEmail);
                    location.setText(userLocation);
                    number.setText(userNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                android.widget.Toast.makeText(MainActivityProfile.this,
                        "Failed to load profile: " + error.getMessage(),
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });

    }
}