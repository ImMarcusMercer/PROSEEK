package com.example.proseekservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;

public class MainActivitySearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_search);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView urprofile = findViewById(R.id.urprofile);
        urprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySearch.this, MainActivityProfile.class);
                startActivity(intent);
            }
        });

        ImageView messages = findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySearch.this, MainActivityMessages.class);
                startActivity(intent);
            }
        });
        TextView header = findViewById(R.id.selection_header);
        header.setText(getIntent().getStringExtra("header"));

        RecyclerView recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //For recycler view
        List<User> usersList = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, usersList);
        recyclerView.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        adapter.setOnItemClickListener(user -> {
            Intent intent = new Intent(MainActivitySearch.this, MainActivityProOp.class);
            intent.putExtra("userId", user.getUserid());
            startActivity(intent);
        });
        adapter.setOnMessageClickListener(task1->{
            Intent intent = new Intent(MainActivitySearch.this, MainActivityConversation.class);
            intent.putExtra("username", task1.getUsername());
            intent.putExtra("userId", task1.getUserid());
            startActivity(intent);
        });


        String initialCategory = getIntent().getStringExtra("category");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (initialCategory != null && !initialCategory.isEmpty()) {
            performSearch(initialCategory, usersList, adapter, currentUserId);
        }
        EditText searchbar = findViewById(R.id.search);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searched = s.toString().trim();
                if (!searched.isEmpty()) {
                    String searchedtext = "Results for \"" + searched+"\"";
                    header.setText(searchedtext);
                    performSearch(searched, usersList, adapter, currentUserId);

                }
                else {
                    header.setText("Search Results");
                    usersList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void performSearch(String category, List<User> usersList, UserAdapter adapter, String currentUserId) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://appdev-69420-default-rtdb.firebaseio.com/").getReference("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User property = ds.getValue(User.class);

                    if (property != null
                            && !property.getUserid().equals(currentUserId)
                            && property.getServices() != null
                            && property.getServices().contains(category.toLowerCase())
                            || property.getUsername().toLowerCase().contains(category.toLowerCase())
                    ) {

                        usersList.add(property);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
