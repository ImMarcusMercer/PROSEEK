package com.example.proseekservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private OnItemClickListener listener;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnMessageClickListener {
        void onMessageClick(User user);
    }

    private OnMessageClickListener messageClickListener;

    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.messageClickListener = listener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_with_job_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getUsername());
        holder.locationTextView.setText(user.getLocation());
        Integer ratingLong = (Integer)user.getRating();
        float rating = ratingLong.floatValue();
        holder.ratingTextView.setRating(rating);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ✅ Made non-static so it can access listener and userList
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView locationTextView;
        public RatingBar ratingTextView;
        public ImageView messageButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.username);
            locationTextView = itemView.findViewById(R.id.location);
            ratingTextView = itemView.findViewById(R.id.rating_preview);
            messageButton = itemView.findViewById(R.id.message_user);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(userList.get(position));
                }
            });
            messageButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (messageClickListener != null && position != RecyclerView.NO_POSITION) {
                    messageClickListener.onMessageClick(userList.get(position));
                }
            });
        }
    }
}
