package com.example.healthcareappdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private Intent intent;
    private MaterialButton sendBtn;
    private TextInputLayout msgInput;
    private String patientName, patientUID, patientToken;

    private MessageAdapter adapter;
    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        Bundle patientInfo = intent.getBundleExtra("patientInfo");
        patientName = patientInfo.getString("name");
        patientUID = patientInfo.getString("UID");
        patientToken = patientInfo.getString("token");

        toolbar = findViewById(R.id.chatTopAppBar);
        toolbar.setTitle(patientName);
        toolbar.setNavigationOnClickListener(navClickHandler);
        toolbar.setOnMenuItemClickListener(menuItemClickHandler);

        sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(sendMsgHandler);

        msgInput = findViewById(R.id.chatInputLayout);

        messageArrayList = new ArrayList<>();
        adapter = new MessageAdapter(getApplicationContext(), messageArrayList);
        recyclerView = findViewById(R.id.chatRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        populateMessage();
    }

    private View.OnClickListener navClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private Toolbar.OnMenuItemClickListener menuItemClickHandler = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.patientCall:
                    handleVideoCall();
                    break;

                case R.id.patientInfo:
                    // TODO: Add intent to patient info
                    break;

                default:
                    return false;
            }
            return false;
        }
    };

    private View.OnClickListener sendMsgHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = msgInput.getEditText().getText().toString();
            if (!msg.isEmpty()) {
                sendMsg(msg);
            }
            msgInput.getEditText().setText("");
        }
    };

    private void sendMsg(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentDoctor = FirebaseAuth.getInstance().getCurrentUser();

        String receiver = patientUID;
        String sender = currentDoctor.getUid();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("receiver", receiver);
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }

    private void populateMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        String doctorUID;
        doctorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(patientUID) && message.getSender().equals(doctorUID)
                    || message.getReceiver().equals(doctorUID) && message.getSender().equals(patientUID)) {
                        messageArrayList.add(message);
                        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleVideoCall() {
        if (patientToken != null) {
            Intent intent = new Intent(getApplicationContext(), OutgoingCallActivity.class);
            intent.putExtra("patientInfo", getIntent().getBundleExtra("patientInfo"));
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "This patient is not available right now", Toast.LENGTH_SHORT).show();
        }
    }
}