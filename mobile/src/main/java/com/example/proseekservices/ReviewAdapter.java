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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.UserViewHolder> {

    private final Context context;
    private final List<Review> reviewsList;
    private OnItemClickListener listener;

    public ReviewAdapter(Context context, List<Review> userList) {
        this.context = context;
        this.reviewsList = userList;
    }

    public int size() {
        return reviewsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Review user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.comment_username.setText(review.getSenderName());
        holder.comment_text1.setText(review.getComment());
        holder.ratingTextView.setRating((float) review.getRating());

    }


    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView comment_username;
        public TextView comment_text1;
        public RatingBar ratingTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_username = itemView.findViewById(R.id.comment_username);
            comment_text1 = itemView.findViewById(R.id.comment_text);
            ratingTextView = itemView.findViewById(R.id.comment_rating);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(reviewsList.get(position));
                }
            });
        }
    }
}
