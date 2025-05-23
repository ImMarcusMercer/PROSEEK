package com.example.proseekservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
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
import java.util.Objects;

public class MainActivityCreateAcc extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    List<String> selectedServices;
    EditText etUsername, etEmail, etPwd, etRepwd, etPhone, etLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_create_acc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.username);
        etEmail = findViewById(R.id.email);
        etPwd = findViewById(R.id.entpassword);
        etRepwd = findViewById(R.id.confirmpass);
        etPhone = findViewById(R.id.phone);
        etLocation = findViewById(R.id.location);

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

        Button create = findViewById(R.id.create);
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

                registerUser();
            }
        });



    }
    public static String formatToExampleString(String input) {
        if (input == null || input.trim().isEmpty()) return "";

        input = input.trim().toLowerCase();
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    private void registerUser()
    {
        String username = formatToExampleString(etUsername.getText().toString().trim());
        String email = etEmail.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        String confirmPassword = etRepwd.getText().toString().trim();
        String number=etPhone.getText().toString().trim();
        String location=formatToExampleString(etLocation.getText().toString().trim());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || number.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null)
                        {
                            String userId = firebaseUser.getUid();
//                            public User(String username, String email, String location, String userid, String number, int rating, boolean availability, List<String> services
                            int rating = 0;
                            List<String> services = new ArrayList<>();
//                            services.add("tutoring");

                            //Adding Data
//                            List<String> newServices = Arrays.asList("Plumbing", "Carpentry");
//
//                            FirebaseDatabase.getInstance()
//                                    .getReference("users")
//                                    .child(userId)
//                                    .child("services")
//                                    .setValue(newServices);
                            User newUser = new User(username, email, location, userId, number, rating, true, selectedServices);
                            databaseRef = FirebaseDatabase.getInstance().getReference("users");
                            databaseRef.child(userId).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            saveUserToPreferences(mAuth.getCurrentUser());
                                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, MainActivity.class));
                                        } else {
                                            Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Sign up failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void saveUserToPreferences(FirebaseUser user) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("userId", user.getUid());
        editor.putString("email", user.getEmail());
        editor.putBoolean("isLoggedIn", true);

        editor.apply();
    }
}