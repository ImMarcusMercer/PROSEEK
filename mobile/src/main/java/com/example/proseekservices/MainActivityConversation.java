//package com.example.prototype;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.*;
//import com.google.firebase.auth.FirebaseAuth;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private EditText inputMessage;
//    private DatabaseReference chatRef;
//    private String chatId;
//    private String currentUserId;
//    private List<Message> messageList = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private MessageAdapter messageAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        // Hardcoded for demo; normally passed via intent
//        String recipientUserId = "ucyKSoGLhNTGJN3GtF3gWLJjmFE3";
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser != null) {
//            currentUserId = currentUser.getUid();
//            chatId = generateChatId(currentUserId, recipientUserId);
//        } else {
//            Log.e("ChatActivity", "User not logged in!");
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show();
//            finish(); // or redirect to login
//        }
//
//        TextView debugText = findViewById(R.id.debug_text);
//
//
//        recyclerView = findViewById(R.id.recycler_view);
//        messageAdapter = new MessageAdapter(ChatActivity.this, messageList, currentUserId);
//
//        inputMessage = findViewById(R.id.edit_message);
//        ImageButton sendButton = findViewById(R.id.btn_send);
//
//
//        chatId = generateChatId(currentUserId, recipientUserId);
//        chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
//
//
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(messageAdapter);
//
//        sendButton.setOnClickListener(v -> {
//            String messageId = chatRef.push().getKey();
//
//            String debugInfo = "CurrentUserID: " + currentUserId + "\nRecipientID: " + recipientUserId + "\nChatID: " + chatId + "\nMessage ID: "+messageId;
//            debugText.setText(debugInfo);
//            String text = inputMessage.getText().toString().trim();
//            if (!TextUtils.isEmpty(text)) {
//                sendMessage(chatId,currentUserId, text);
//                inputMessage.setText("");
//            }
//        });
//
//        listenForMessages(chatId);
//    }
//
////    public void sendMessage(String chatId, String senderId, String messageText) {
////        String messageId = chatRef.push().getKey();
////        if (messageId != null) {
////            Message message = new Message(senderId, messageText, System.currentTimeMillis());
////            chatRef.child(messageId).setValue(message);
////        }
////    }
//    public void sendMessage(String chatId, String senderId, String messageText) {
//        String messageId = chatRef.push().getKey();
//        if (messageId != null && !TextUtils.isEmpty(senderId) && !TextUtils.isEmpty(messageText)) {
//            Message message = new Message(senderId, messageText, System.currentTimeMillis());
//            chatRef.child(messageId).setValue(message);
//        } else {
//            Log.e("ChatDebug", "Failed to send message: missing fields");
//        }
//    }
//
//
//    public void listenForMessages(String chatId) {
//        recyclerView = findViewById(R.id.recycler_view);
//        messageAdapter = new MessageAdapter(ChatActivity.this, messageList, currentUserId);
//        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
//
//        chatRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
//                Message msg = snapshot.getValue(Message.class);
//                if (msg != null) {
//                    messageList.add(msg);
//                    messageAdapter.notifyItemInserted(messageList.size() - 1);
//                    recyclerView.scrollToPosition(messageList.size() - 1);
//                }
//            }
//
//
//            @Override public void onCancelled(DatabaseError error) {}
//            @Override public void onChildChanged(DataSnapshot ds, String prev) {}
//            @Override public void onChildRemoved(DataSnapshot ds) {}
//            @Override public void onChildMoved(DataSnapshot ds, String prev) {}
//        });
//    }
//
//    private String generateChatId(String user1, String user2) {
//        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
//    }
//}



package com.example.proseekservices;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityConversation extends AppCompatActivity {

    private EditText inputMessage;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private DatabaseReference chatRef;
    private String chatId;
    private String currentUserId;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conversation);

        //Header
        String username = getIntent().getStringExtra("username");
        String recipientUserId = getIntent().getStringExtra("userId");
//        Toast.makeText(this, "User ID: " + recipientUserId, Toast.LENGTH_SHORT).show();

        TextView userTextView = findViewById(R.id.username);
        userTextView.setText(username);
        TextView userTextView1 = findViewById(R.id.username1);
        userTextView1.setText(username);

        inputMessage = findViewById(R.id.message_input);
        recyclerView = findViewById(R.id.recycler_view_conversation);
        ImageButton sendButton = findViewById(R.id.send_button);


        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        chatId = generateChatId(currentUserId, recipientUserId);
//        Toast.makeText(this, chatId, Toast.LENGTH_SHORT).show();

        chatRef = FirebaseDatabase.getInstance()
                .getReference("chats").child(chatId).child("messages");


        messageAdapter = new MessageAdapter(this, messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> sendMessage(recipientUserId));

        loadMessages();
    }

    private void sendMessage(String recipient) {
        String text = inputMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            String messageId = chatRef.push().getKey();
            Message message = new Message(recipient, currentUserId, text, System.currentTimeMillis());

            if (messageId != null) {
                chatRef.child(messageId).setValue(message);
                inputMessage.setText("");
//                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateChatId(String user1, String user2) {
        if (user1 == null) user1 = "nullUser1";
        if (user2 == null) user2 = "nullUser2";
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }


    private void loadMessages() {
        chatRef = FirebaseDatabase
                .getInstance()
                .getReference("chats")
                .child(chatId)
                .child("messages");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message msg = data.getValue(Message.class);
                    if (msg != null) {
                        messageList.add(msg);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivityConversation.this, "Error on database side", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
