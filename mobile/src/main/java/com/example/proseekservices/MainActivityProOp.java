package com.example.proseekservices;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityProOp extends AppCompatActivity {
    private TextView otherusername;
    private TextView otherusername1;
    private TextView othernumber;
    private TextView otherlocation;
    private TextView otheremail;
    private String otherUserId;
    private TextView status;
    private ReviewAdapter reviewAdapter;
    private String currentUserId;
    private DatabaseReference reviewRef;
    private List<Review> reviewsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String currentUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_pro_op);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI references
        otherusername1 = findViewById(R.id.username1);
        otherusername = findViewById(R.id.other_username);
        othernumber = findViewById(R.id.other_number);
        otherlocation = findViewById(R.id.other_location);
        otheremail = findViewById(R.id.other_email);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.recycler_view_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // IDs
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference currentuserRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUserId)
                .child("username");

        currentuserRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    currentUserName = snapshot.getValue(String.class);
                } else {
                    Log.d("USERNAME", "No username found for user.");
                }
            } else {
                Log.e("USERNAME", "Error getting username", task.getException());
            }
        });

        otherUserId = getIntent().getStringExtra("userId");

        if (otherUserId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(otherUserId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User property = snapshot.getValue(User.class);
                    if (property != null && property.getUserid().equals(otherUserId)) {
                        otherusername.setText(property.getUsername());
                        otherusername1.setText(property.getUsername());
                        othernumber.setText(property.getNumber());
                        otherlocation.setText(property.getLocation());
                        otheremail.setText(property.getEmail());

                        // Rating setup
                        Integer ratingLong = (Integer) property.getRating();
                        RatingBar ratingBar = findViewById(R.id.profile_rating);
                        float rating = ratingLong != null ? ratingLong.floatValue() : 0f;
                        ratingBar.setRating(rating);

                        // Availability color
                        LinearLayout availabilityColor = findViewById(R.id.availability_color);
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setCornerRadius(20f);
                        drawable.setShape(GradientDrawable.RECTANGLE);
                        if (property.getAvailability()) {
                            status.setText(R.string.available_for_service_tag);
                            drawable.setColor(ContextCompat.getColor(MainActivityProOp.this, R.color.green));
                        } else {
                            status.setText(R.string.user_is_unavailable_tag);
                            drawable.setColor(ContextCompat.getColor(MainActivityProOp.this, R.color.red));
                        }
                        availabilityColor.setBackground(drawable);

                        // ✅ Now safe to initialize ReviewAdapter
                        reviewAdapter = new ReviewAdapter(MainActivityProOp.this, reviewsList);
                        recyclerView.setAdapter(reviewAdapter);

                        // ✅ Load reviews after adapter is set
                        loadReviews();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Message button setup
            LinearLayout messageUser = findViewById(R.id.messageUser);
            messageUser.setOnClickListener(task -> {
                Intent intent = new Intent(MainActivityProOp.this, MainActivityConversation.class);
                intent.putExtra("username", otherusername.getText().toString());
                intent.putExtra("userId", otherUserId);
                startActivity(intent);
            });

            // Review button
            EditText reviewInput = findViewById(R.id.user_review_input_text);
            ImageButton sendReviewButton = findViewById(R.id.send_review_button);
            sendReviewButton.setOnClickListener(task -> {
                if(!reviewInput.getText().toString().isEmpty()) {
                    sendReview(otherUserId, reviewInput);
                }
                else{
                    Toast.makeText(MainActivityProOp.this, "Review cannot be empty", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    private void sendReview(String recipient, EditText inputReview) {
        String text = inputReview.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            String messageId = reviewRef.push().getKey();
            RatingBar ratingBar = findViewById(R.id.ratingBar);
            float ratingValue = ratingBar.getRating();
            Review review = new Review(currentUserId, currentUserName, text, System.currentTimeMillis(), ratingValue);
            if (messageId != null) {
                reviewRef.child(messageId).setValue(review);
                inputReview.setText("");
//                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadReviews() {
        reviewRef = FirebaseDatabase
                .getInstance()
                .getReference("reviews")
                .child(otherUserId)
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
                Toast.makeText(MainActivityProOp.this, "Error on database side", Toast.LENGTH_SHORT).show();
            }
        });
    }

}