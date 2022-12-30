package com.example.healthcareapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Message> msgArray;
    private FirebaseUser currentUser;


    public static final int MSG_DISPLAY_LEFT = 0;
    public static final int MSG_DISPLAY_RIGHT = 1;

    public MessageAdapter(Context context, ArrayList<Message> msgArray) {
        this.context = context;
        this.msgArray = msgArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView msg;
        private ImageView doctorPfp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msg = itemView.findViewById(R.id.message);
            doctorPfp = itemView.findViewById(R.id.doctorPfp);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (msgArray.get(position).getSender().equals(currentUser.getUid())) {
            return MSG_DISPLAY_RIGHT;
        }
        else {
            return MSG_DISPLAY_LEFT;
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_DISPLAY_LEFT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receive, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message currentMsg = msgArray.get(position);

        holder.msg.setText(currentMsg.getMessage());

        // TODO: Add logics to add pfp if the msg belongs to receiver
    }

    @Override
    public int getItemCount() {
        return msgArray.size();
    }
}
