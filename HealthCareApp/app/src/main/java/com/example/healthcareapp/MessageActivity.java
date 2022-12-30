package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.listeners.UsersListeners;
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

    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private DoctorInfoFragment doctorInfoFragment;
    private TextInputLayout chatTextInput;
    private MaterialButton sendBtn;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> msgList;
    private String doctorUID, patientUID, doctorName, doctorToken;

    private UsersListeners usersListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.chatRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        toolbar = findViewById(R.id.chatTopAppBar);

        Intent intent = getIntent();
        Bundle doctorInfo = intent.getBundleExtra("doctorInfo");
        doctorName = doctorInfo.getString("name");
        doctorUID = doctorInfo.getString("UID");
        doctorToken = doctorInfo.getString("token");
        patientUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        toolbar.setTitle(doctorName);
        toolbar.setNavigationOnClickListener(navOnClickHandler);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);

        doctorInfoFragment = new DoctorInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("doctorName", doctorName);
        doctorInfoFragment.setArguments(bundle);

        chatTextInput = findViewById(R.id.chatInputLayout);
        sendBtn = findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(sendMsgHandler);

        msgList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(), msgList);
        recyclerView.setAdapter(messageAdapter);
        populateMessage();


    }

    private View.OnClickListener navOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
        }
    };

    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.doctorInfo:
                    doctorInfoFragment.show(getSupportFragmentManager(), doctorInfoFragment.getTag());
                    break;
                case R.id.doctorCall:
                    handleVideoCall();
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
            String msg = chatTextInput.getEditText().getText().toString();
            if (!msg.isEmpty()) {
                sendMsg(msg);
            }
            chatTextInput.getEditText().setText("");
        }
    };

    private void sendMsg(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String receiver = doctorUID;
        String sender = patientUID;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("receiver", receiver);
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }

    private void populateMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(patientUID) && message.getSender().equals(doctorUID)
                            || message.getReceiver().equals(doctorUID) && message.getSender().equals(patientUID)) {
                        msgList.add(message);
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
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
        if (doctorToken != null) {
            Intent intent = new Intent(getApplicationContext(), OutgoingCallActivity.class);
            intent.putExtra("doctorInfo", getIntent().getBundleExtra("doctorInfo"));
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "This doctor is not available right now", Toast.LENGTH_SHORT).show();
        }
    }
}