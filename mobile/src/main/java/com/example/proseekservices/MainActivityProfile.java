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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.value.qual.StringVal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityProfile extends AppCompatActivity {

    TextView username;
    TextView email;
    TextView location;
    TextView number;
    private DatabaseReference reviewRef;
    private List<Review> reviewsList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private RecyclerView recyclerView;

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

                    recyclerView = findViewById(R.id.recycler_user_reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivityProfile.this));
                    reviewAdapter = new ReviewAdapter(MainActivityProfile.this, reviewsList);
                    recyclerView.setAdapter(reviewAdapter);
                    loadReviews();
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
    private void loadReviews() {
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reviewRef = FirebaseDatabase
                .getInstance()
                .getReference("reviews")
                .child(currentUserId)
                .child("review");

        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Review msg = data.getValue(Review.class);
                    if (msg != null) {
                        reviewsList.add(msg);
                    }
                }
                reviewAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(reviewAdapter.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivityProfile.this, "Error on database side", Toast.LENGTH_SHORT).show();
            }
        });
    }
}