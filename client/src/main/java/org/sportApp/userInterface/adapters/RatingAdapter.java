package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class RatingAdapter extends BaseAdapter<UserDto, BaseAdapter.BaseViewHolder<UserDto>> {

    public RatingAdapter(List<UserDto> items, OnItemClickListener<UserDto> listener) {
        super(items, R.layout.item_rating, listener, RatingAdapter.RatingViewHolder::new);
    }
    public static class RatingViewHolder extends BaseViewHolder<UserDto> {
        private final TextView friendNameTextView;
        private final TextView friendAgeTextView;

        private final ImageView cupFirstImageView;

        private final ImageView cupSecondImageView;

        private final ImageView cupThirdImageView;

        private final TextView positionTextView;


        public RatingViewHolder(View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
            friendAgeTextView = itemView.findViewById(R.id.friendAgeTextView);
            cupFirstImageView = itemView.findViewById(R.id.cupFirstImageView);
            cupSecondImageView = itemView.findViewById(R.id.cupSecondImageView);
            cupThirdImageView = itemView.findViewById(R.id.cupThirdImageView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener<UserDto> listener) {
            super.bind(user, listener);
            Log.d("myTag", user.toString());
            int position = getAdapterPosition();
            if (position >= 3) {
                cupFirstImageView.setVisibility(View.GONE);
                cupSecondImageView.setVisibility(View.GONE);
                cupThirdImageView.setVisibility(View.GONE);
                positionTextView.setText(String.valueOf(position + 1));
            }
            else if (position == 0){
                cupSecondImageView.setVisibility(View.GONE);
                cupThirdImageView.setVisibility(View.GONE);
                positionTextView.setVisibility(View.GONE);
            }
            else if (position == 1) {
                cupFirstImageView.setVisibility(View.GONE);
                cupThirdImageView.setVisibility(View.GONE);
                positionTextView.setVisibility(View.GONE);
            }
            else {
                cupFirstImageView.setVisibility(View.GONE);
                cupSecondImageView.setVisibility(View.GONE);
                positionTextView.setVisibility(View.GONE);
            }
            friendNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            friendAgeTextView.setText(32 + " years.");
        }

    }
}