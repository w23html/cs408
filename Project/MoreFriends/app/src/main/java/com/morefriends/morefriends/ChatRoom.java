package com.morefriends.morefriends;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {

    private static final String TAG = ChatRoom.class.getName();
    private static String sUserId;
    private String id;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 40;


    public static final String USER_ID_KEY = "userId";
    private EditText etMessage;
    private Button btSend;

    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private boolean mFirstLoad;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        // User login
        if (getIntent() == null || getIntent().getExtras() == null || getIntent().getExtras().getString("id") == null) {
            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            finish();
        }
        id = getIntent().getExtras().getString("id");
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser(); // Start with current user
        }
        handler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 1000);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }

    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();

        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatRoom.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                Message message = new Message();
                message.setUserId(sUserId);
                message.setBody(body);
                message.put("id", id);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query1.whereEqualTo("id", sUserId);
        query1.whereEqualTo("userId", id);
        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query2.whereEqualTo("id", id);
        query2.whereEqualTo("userId", sUserId);
        List<ParseQuery<Message>> q = new LinkedList<>();
        q.add(query1);
        q.add(query2);

        ParseQuery<Message> query = ParseQuery.or(q);

        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL

        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    Collections.reverse(mMessages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }
}
