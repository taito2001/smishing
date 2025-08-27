package com.example.smishingdetectionapp.chat;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smishingdetectionapp.R;
import com.example.smishingdetectionapp.chat.db.ChatDatabase;
import com.example.smishingdetectionapp.chat.db.ChatMessageEntity;

import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Clear history button
        findViewById(R.id.clearHistoryButton).setOnClickListener(v -> confirmAndDeleteHistory());

        RecyclerView historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChatAdapter chatAdapter = new ChatAdapter(this);
        historyRecyclerView.setAdapter(chatAdapter);

        // Load messages in background
        new Thread(() -> {
            ChatDatabase db = ChatDatabase.getInstance(getApplicationContext());
            List<ChatMessageEntity> allMessages = db.chatMessageDao().getAll();

            runOnUiThread(() -> {
                if (allMessages == null || allMessages.isEmpty()) {
                    Toast.makeText(this, "No chat history available.", Toast.LENGTH_SHORT).show();
                } else {
                    for (ChatMessageEntity e : allMessages) {
                        chatAdapter.addMessage(new ChatMessage(
                                e.text,
                                e.sender.equalsIgnoreCase("user") ? ChatMessage.USER : ChatMessage.BOT
                        ));
                    }
                    historyRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1); // auto-scroll to bottom
                }
            });
        }).start();
    }

    private void confirmAndDeleteHistory() {
        new AlertDialog.Builder(this)
                .setTitle("Delete History")
                .setMessage("Are you sure you want to clear all chat history?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        ChatDatabase db = ChatDatabase.getInstance(getApplicationContext());
                        db.chatMessageDao().deleteAll();

                        runOnUiThread(() ->
                                Toast.makeText(this, "Chat history cleared.", Toast.LENGTH_SHORT).show()
                        );
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
