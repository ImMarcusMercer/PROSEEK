package com.example.proseekservices;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityEditProf extends AppCompatActivity {

    private EditText usernameInput, emailInput, locationInput, numberInput;
    private List<String> selectedServices;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_edit_prof);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inputs
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        locationInput = findViewById(R.id.location);
        numberInput = findViewById(R.id.number);

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        uid = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        // Load current profile data
        userRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameInput.setText(snapshot.child("username").getValue(String.class));
                    emailInput.setText(snapshot.child("email").getValue(String.class));
                    locationInput.setText(snapshot.child("location").getValue(String.class));
                    numberInput.setText(snapshot.child("number").getValue(String.class));

                    for (com.google.firebase.database.DataSnapshot serviceSnap : snapshot.child("services").getChildren()) {
                        String service = serviceSnap.getValue(String.class);
                        if (service == null) continue;
                        switch (service.toLowerCase()) {
                            case "tutoring": cbTutoring.setChecked(true); break;
                            case "house chores": cbChores.setChecked(true); break;
                            case "project": cbProject.setChecked(true); break;
                            case "assistant": cbAssistant.setChecked(true); break;
                            case "healthcare": cbHealthcare.setChecked(true); break;
                            case "food delivery": cbFoodDelivery.setChecked(true); break;
                            case "photography": cbPhotography.setChecked(true); break;
                            case "rentals": cbRentals.setChecked(true); break;
                            case "machinery": cbMachinery.setChecked(true); break;
                            case "tailoring": cbTailoring.setChecked(true); break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Toast.makeText(MainActivityEditProf.this, "Failed to load profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Save button logic
        Button saveButton = findViewById(R.id.save_edit);
        saveButton.setOnClickListener(task -> {
            if (!InternetManager.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String loc = locationInput.getText().toString().trim();
            String num = numberInput.getText().toString().trim();

            selectedServices = new ArrayList<>();
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

            Map<String, Object> updates = new HashMap<>();
            updates.put("username", name);
            updates.put("email", email);
            updates.put("location", loc);
            updates.put("number", num);
            updates.put("services", selectedServices);

            userRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
