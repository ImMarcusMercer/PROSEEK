package com.example.proseekservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivityHomeScr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_home_scr);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        EditText searchbar = findViewById(R.id.searchbar);
        searchbar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searched = searchbar.getText().toString().trim();
                if (!searched.isEmpty()) {
                    Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                    intent.putExtra("category", searched);
                    intent.putExtra("header", "Searched \"" + searched+"\"");
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });

        LinearLayout tutoring = findViewById(R.id.tutoring);
        tutoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "tutoring");
                intent.putExtra("header","Tutoring");
                startActivity(intent);
            }
        });

        LinearLayout housechores = findViewById(R.id.housechores);
        housechores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "house chores");
                intent.putExtra("header","House Chores");
                startActivity(intent);
            }
        });

        LinearLayout project = findViewById(R.id.project);
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "project");
                intent.putExtra("header","Project");
                startActivity(intent);
            }
        });

        LinearLayout assistant = findViewById(R.id.assistant);
        assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "assistant");
                intent.putExtra("header","Assistant");
                startActivity(intent);
            }
        });

        LinearLayout healthcare = findViewById(R.id.healthcare);
        healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "healthcare");
                intent.putExtra("header","Healthcare");
                startActivity(intent);
            }
        });

        ImageView urprofile = findViewById(R.id.urprofile);
        urprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivityProfile.class);
                startActivity(intent);
            }
        });

        ImageView messages = findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivityMessages.class);
                startActivity(intent);

            }
        });

        LinearLayout fooddelivery = findViewById(R.id.fooddelivery);
        fooddelivery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "food delivery");
                intent.putExtra("header","Food Delivery");
                startActivity(intent);
        }
        });
        LinearLayout photograpy = findViewById(R.id.photography);
        photograpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "photography");
                intent.putExtra("header","Photography");
                startActivity(intent);
            }
        });

        LinearLayout rentals = findViewById(R.id.rentals);
        rentals.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "rentals");
                intent.putExtra("header","Rentals");
                startActivity(intent);
            }
        });

        LinearLayout machinery = findViewById(R.id.machinery);
        machinery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "machinery");
                intent.putExtra("header","Machinery");
                startActivity(intent);
            }
        });

        LinearLayout tailoring = findViewById(R.id.tailoring);
        tailoring.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivityHomeScr.this, MainActivitySearch.class);
                intent.putExtra("category", "tailoring");
                intent.putExtra("header","Tailoring");
                startActivity(intent);
            }
        });


    }
}