package com.example.apoc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apoc.R;
import com.example.apoc.Storage.ImagesDB;
import com.example.apoc.Displays.GridDisplay;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.JoinRequestViewHolder> {
    private ArrayList<User> itemsList;
    private RequestAdapter.OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onRequestApprove(int position);

        void onRequestDelete(int position);
    }

    public void setOnItemClickListener(RequestAdapter.OnItemClickListener listener) {
        myListener = listener;
    }

    public static class JoinRequestViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView nickname;
        public TextView email;
        public TextView phone;
        public Button approve;
        public Button delete;
        public GridLayout fears;
        public GridLayout skills;
        public Context context;

        public JoinRequestViewHolder(@NonNull View itemView, final RequestAdapter.OnItemClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.request_image);
            nickname = itemView.findViewById(R.id.request_nick_name);
            email = itemView.findViewById(R.id.request_email);
            phone = itemView.findViewById(R.id.request_phone);
            approve = itemView.findViewById(R.id.request_approve);
            delete = itemView.findViewById(R.id.request_delete);
            fears = itemView.findViewById(R.id.request_fears);
            skills = itemView.findViewById(R.id.request_skills);
            context = itemView.getContext();

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRequestApprove(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRequestDelete(position);
                        }
                    }
                }
            });
        }
    }

    public RequestAdapter(ArrayList<User> list) {
        itemsList = list;
    }

    @NonNull
    @Override
    public JoinRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_view, parent,
                false);
        JoinRequestViewHolder evh = new JoinRequestViewHolder(v, myListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinRequestViewHolder holder, int position) {
        User currentItem = itemsList.get(position);
        holder.nickname.setText(currentItem.getNickName());
        holder.email.setText(currentItem.getEmail());
        holder.phone.setText(currentItem.getPhone());
        GridDisplay gridDisplay = new GridDisplay(holder.context,currentItem, holder.fears, holder.skills, false, 6);

        ImagesDB.showCircleImage(currentItem.getImageUrl(), holder.image,holder.image.getContext());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void deleteRequest(int position){
        itemsList.remove(position);
        notifyDataSetChanged();
    }

    public User getUserByPosition(int position){
        return itemsList.get(position);
    }

}