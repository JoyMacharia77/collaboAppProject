package com.joymacharia.collaboapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MessageActivity extends AppCompatActivity implements TextWatcher {

    private String name;
    private WebSocket webSocket;
    //SERVER PATH HAS IP ADDRESS AND PORT NUMBER THAT SERVER IS RUNNING
    private String SERVER_PATH = "ws://192.168.0.107:3000";
    private EditText messageEdit;
    private RecyclerView recyclerView;
    private View sendBtn, pickImgBtn;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        name = getIntent().getStringExtra("name");
        initiateSocketConnection();
    }

    private void initiateSocketConnection()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if(string.isEmpty())
        {
            resetMessageEdit();
        }else {
            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void resetMessageEdit()
    {
        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);
    }

    private class SocketListener extends WebSocketListener
    {
        //Called when Client Successfully Connects With The Server
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(()->{
                Toast.makeText(MessageActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

                initializeView();
            });
        }

        // Called when we receive any message in form of String
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            //Work of Server is to Receive the msg and send it to everyone except the sender
            //Here,we accept the message and display in recycler view
            runOnUiThread(()->{
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);

                    messageAdapter.addItem(jsonObject);

                    //To automatically Scroll Down once messages are sent or received
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() -1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void initializeView()
    {
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendMsg);
        pickImgBtn = findViewById(R.id.pickImgBtn);
        recyclerView = findViewById(R.id.messageRecyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEdit.addTextChangedListener(this);

        //Message in Edit Text is put in JSON Object which is the sent to the Server
        sendBtn.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("message", messageEdit.getText().toString());

                //send to SERVER
                webSocket.send(jsonObject.toString());

                //To show the msg in recyclerView
                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                //To automatically Scroll Down once messages are sent or received
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() -1);

                //Empty Message Edit Text After Sending The Object
                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        //To Open Gallery For User To Send Image
        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick Image"),
                    IMAGE_REQUEST_ID);
        });
    }

    //Accept The Selected Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK)
        {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImage(Bitmap image)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                 Base64.DEFAULT);

        //SEND WITH SERVER
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name",name);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            //To show the msg in recyclerView
            jsonObject.put("isSent", true);
            messageAdapter.addItem(jsonObject);

            //To automatically Scroll Down once messages are sent or received
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() -1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
