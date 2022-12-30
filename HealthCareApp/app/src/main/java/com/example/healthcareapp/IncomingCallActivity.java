package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.network.APIClient;
import com.example.healthcareapp.network.APIService;
import com.example.healthcareapp.utilities.Constants;
import com.google.android.material.button.MaterialButton;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {
    private TextView doctorNameTextView;
    private String doctorName, inviterToken;
    private String meetingRoom;
    private Intent intent;

    private MaterialButton acceptCallBtn, rejectCallBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        intent = getIntent();
        doctorName = intent.getStringExtra("doctorName");
        meetingRoom = intent.getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM);

        doctorNameTextView = findViewById(R.id.doctorName);
        doctorNameTextView.setText(doctorName);

        acceptCallBtn = findViewById(R.id.acceptCallButton);
        acceptCallBtn.setOnClickListener(acceptCallHandler);
        rejectCallBtn = findViewById(R.id.rejectCallButton);
        rejectCallBtn.setOnClickListener(rejectCallHandler);

        intent = getIntent();
        inviterToken = intent.getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN);
    }

    private void sendResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        APIClient.getClient().create(APIService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {

                        try {
                            URL url = new URL("https://meet.jit.si");
                            JitsiMeetConferenceOptions conferenceOptions =
                                    new JitsiMeetConferenceOptions.Builder()
                                            .setServerURL(url)
                                            .setRoom(meetingRoom)
                                            .build();
                            JitsiMeetActivity.launch(getApplicationContext(), conferenceOptions);
                            finish();
                        }catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invitation rejected", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.d("IncomingCall", response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("IncomingCall", t.getMessage());
                finish();
            }
        });
    }
    private View.OnClickListener acceptCallHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED, inviterToken);
        }
    };

    private View.OnClickListener rejectCallHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendResponse(Constants.REMOTE_MSG_INVITATION_REJECTED, inviterToken);
        }
    };

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)) {
                    Toast.makeText(getApplicationContext(), "Invitation cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}