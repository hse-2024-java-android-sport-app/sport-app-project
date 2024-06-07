package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class FindCoachAdapter extends BaseAdapter<UserDto, BaseAdapter.BaseViewHolder<UserDto>> {

    public FindCoachAdapter(List<UserDto> items, OnItemClickListener<UserDto> listener) {
        super(items, R.layout.item_find_coach, listener, FindCoachViewHolder::new);
    }

    public static class FindCoachViewHolder extends BaseViewHolder<UserDto> {
        private final TextView coachNameTextView;
        private final TextView coachAgeTextView;

        public FindCoachViewHolder(View itemView) {
            super(itemView);
            coachNameTextView = itemView.findViewById(R.id.coachNameTextView);
            coachAgeTextView = itemView.findViewById(R.id.coachAgeTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener<UserDto> listener) {
            super.bind(user, listener);
            coachNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            coachAgeTextView.setText(32 + " years.");
        }
    }
}
