package com.joymacharia.collaboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChatActivity extends AppCompatActivity {
    private EditText editTextChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EditText editTextChat = findViewById(R.id.editTextChat);

        findViewById(R.id.joinChatBtn)
                .setOnClickListener(v -> {

                    Intent intent = new Intent(this,MessageActivity.class);
                    intent.putExtra("name", editTextChat.getText().toString());
                    startActivity(intent);
                });
    }

}
