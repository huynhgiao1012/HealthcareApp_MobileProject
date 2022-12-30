package com.example.healthcareappdoctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private final ArrayList<Message> messageArrayList;
    private FirebaseUser currentUser;

    public static final int MSG_DISPLAY_LEFT = 0;
    public static final int MSG_DISPLAY_RIGHT = 1;

    public MessageAdapter (Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message =  messageArrayList.get(position);

        holder.msgView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView msgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msgView = itemView.findViewById(R.id.message);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (messageArrayList.get(position).getSender().equals(currentUser.getUid())) {
            return MSG_DISPLAY_RIGHT;
        }
        else {
            return MSG_DISPLAY_LEFT;
        }
    }
}
