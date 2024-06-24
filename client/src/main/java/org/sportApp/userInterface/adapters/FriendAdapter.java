package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class FriendAdapter extends BaseAdapter<UserDto, BaseAdapter.BaseViewHolder<UserDto>> {
    public FriendAdapter(List<UserDto> items, OnItemClickListener<UserDto> listener) {
        super(items, R.layout.item_friends, listener, FriendViewHolder::new);
    }

    public static class FriendViewHolder extends BaseViewHolder<UserDto> {
        private final TextView friendNameTextView;
        private final TextView friendAgeTextView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
            friendAgeTextView = itemView.findViewById(R.id.friendAgeTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener<UserDto> listener) {
            super.bind(user, listener);
            friendNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            friendAgeTextView.setText(user.getAge() + " years.");
        }
    }
}
