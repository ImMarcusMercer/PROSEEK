package com.example.proseekservices;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivityEditProf extends AppCompatActivity {
    private TextView username;
    private TextView email;
    private TextView location;
    private TextView number;

    private List<String> selectedServices;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_edit_prof);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CheckBox cbTutoring = findViewById(R.id.cbTutoring);
        CheckBox cbChores = findViewById(R.id.cbChores);
        CheckBox cbProject = findViewById(R.id.cbProject);
        CheckBox cbAssistant = findViewById(R.id.cbAssistant);
        CheckBox cbHealthcare = findViewById(R.id.cbHealthcare);
        CheckBox cbFoodDelivery = findViewById(R.id.cbFoodDelivery);
        CheckBox cbPhotography = findViewById(R.id.cbPhotography);
        CheckBox cbRentals = findViewById(R.id.cbRentals);
        CheckBox cbMachinery = findViewById(R.id.cbMachinery);
        CheckBox cbTailoring = findViewById(R.id.cbTailoring);
        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
        com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("services")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                        List<String> currentServices = new ArrayList<>();
                        for (com.google.firebase.database.DataSnapshot serviceSnap : snapshot.getChildren()) {
                            String service = serviceSnap.getValue(String.class);
                            if (service != null) currentServices.add(service.toLowerCase());
                        }

                        // Match service names to checkboxes
                        if (currentServices.contains("tutoring")) cbTutoring.setChecked(true);
                        if (currentServices.contains("house chores")) cbChores.setChecked(true);
                        if (currentServices.contains("project")) cbProject.setChecked(true);
                        if (currentServices.contains("assistant")) cbAssistant.setChecked(true);
                        if (currentServices.contains("healthcare")) cbHealthcare.setChecked(true);
                        if (currentServices.contains("food delivery")) cbFoodDelivery.setChecked(true);
                        if (currentServices.contains("photography")) cbPhotography.setChecked(true);
                        if (currentServices.contains("rentals")) cbRentals.setChecked(true);
                        if (currentServices.contains("machinery")) cbMachinery.setChecked(true);
                        if (currentServices.contains("tailoring")) cbTailoring.setChecked(true);
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                        android.widget.Toast.makeText(MainActivityEditProf.this,
                                "Failed to load services: " + error.getMessage(),
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
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


                    StringBuilder servicesBuilder = new StringBuilder();
                    for (com.google.firebase.database.DataSnapshot serviceSnap : snapshot.child("services").getChildren()) {
                        String service = serviceSnap.getValue(String.class);
                        if (service != null) {
                            if (servicesBuilder.length() > 0) servicesBuilder.append(", ");
                            servicesBuilder.append(service);
                        }
                    }
                    username = findViewById(R.id.username);
                    email = findViewById(R.id.email);
                    location = findViewById(R.id.location);
                    number = findViewById(R.id.number);


                    // Set to views
                    username.setText(fullName);
                    email.setText(userEmail);
                    location.setText(userLocation);
                    number.setText(userNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                android.widget.Toast.makeText(MainActivityEditProf.this,
                        "Failed to load profile: " + error.getMessage(),
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });



        Button create = findViewById(R.id.save_edit);
        create.setOnClickListener(task-> {
            if(InternetManager.isInternetAvailable(this)){
                selectedServices=new ArrayList<>();

                if (cbTutoring.isChecked()) selectedServices.add("tutoring");
                if (cbChores.isChecked()) selectedServices.add("house chores");
                if (cbProject.isChecked()) selectedServices.add("project");
                if (cbAssistant.isChecked()) selectedServices.add("assistant");
                if (cbHealthcare.isChecked()) selectedServices.add("healthcare");
                if (cbFoodDelivery.isChecked()) selectedServices.add("food delivery");
                if (cbPhotography.isChecked()) selectedServices.add("photography");
                if (cbRentals.isChecked()) selectedServices.add("rentals");
                if (cbMachinery.isChecked()) selectedServices.add("machinery");
                if (cbTailoring.isChecked()) selectedServices.add("tailoring");

                com.google.firebase.database.FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(uid)
                        .child("services")
                        .setValue(selectedServices)
                        .addOnSuccessListener(unused ->
                                android.widget.Toast.makeText(this, "Profile updated.", android.widget.Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                android.widget.Toast.makeText(this, "Failed to update: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show());
            }
        });
    }
}