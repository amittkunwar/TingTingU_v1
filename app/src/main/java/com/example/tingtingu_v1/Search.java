package com.example.tingtingu_v1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Search extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    RecyclerView recyclerView;
    EditText message;
    ImageView send;
    List<MessageModel> list;
    MessageAdapter adapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        // Set up RecyclerView
        list = new ArrayList<>();
        adapter = new MessageAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Send button click listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = message.getText().toString().trim();
                Log.d(TAG, "User message: " + question); // Debug log
                if (question.isEmpty()) {
                    Toast.makeText(Search.this, "Please ask your question", Toast.LENGTH_SHORT).show();
                } else {
                    addToChat(question, MessageModel.SENT_BY_ME);
                    message.setText("");
                    callAPI(question);
                }
            }

            private void callAPI(String question) {
                // Add typing indication
                list.add(new MessageModel("Typing...", MessageModel.SENT_BY_BOT));
                adapter.notifyDataSetChanged();

                JSONObject jsonObject = new JSONObject();
                try {
                    // Building the request object
                    jsonObject.put("model", "gemini-1.5-flash");

                    // Creating the "messages" array for chat completion format
                    JSONArray messagesArray = new JSONArray();
                    JSONObject messageObject = new JSONObject();
                    messageObject.put("role", "user");
                    messageObject.put("content", question);
                    messagesArray.put(messageObject);

                    jsonObject.put("messages", messagesArray);
                    jsonObject.put("max_tokens", 3000);  // Set max_tokens
                    jsonObject.put("temperature", 0.9f);  // Slight randomness for variation

                } catch (JSONException e) {
                    Log.e(TAG, "JSON error: " + e.getMessage());
                }

                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url("https://api.openai.com/v1/chat/completions")
                        .header("Authorization", "Bearer AIzaSyBLDctGnCP-DpDPPhoUEIR6ZmdOylhO5mM") // Replace with actual API key
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.body() != null) {
                            String responseBody = response.body().string();
                            Log.d(TAG, "Response: " + responseBody); // Log the response
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                                    String result = choicesArray.getJSONObject(0).getJSONObject("message").getString("content");
                                    addResponse(result.trim());
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                                }
                            } else {
                                Log.e(TAG, "Failed response: " + response.message() + " Code: " + response.code());
                                addResponse("Failed to load response: " + response.message());
                            }
                        } else {
                            Log.e(TAG, "Response body is null");
                            addResponse("Failed to load response: Empty body");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, "Request failed: " + e.getMessage()); // Log the error message
                        addResponse("Failed to load response: " + e.getMessage());
                    }

                    private void addResponse(String responseText) {
                        // Remove "Typing..." message
                        list.remove(list.size() - 1);
                        addToChat(responseText, MessageModel.SENT_BY_BOT);
                    }
                });
            }

            private void addToChat(String messageText, String sentBy) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.add(new MessageModel(messageText, sentBy));
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                });
            }
        });
    }
}
