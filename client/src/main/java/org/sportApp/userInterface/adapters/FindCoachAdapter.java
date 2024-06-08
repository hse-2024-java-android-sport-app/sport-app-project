package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.utils.UserManager;

import java.util.List;

public class FindCoachAdapter extends BaseAdapter<UserDto, BaseAdapter.BaseViewHolder<UserDto>> {

    public FindCoachAdapter(List<UserDto> items, OnItemClickListener<UserDto> listener) {
        super(items, R.layout.item_find_coach, listener, FindCoachViewHolder::new);
    }

    public static class FindCoachViewHolder extends BaseViewHolder<UserDto> {
        private final TextView coachNameTextView;
        private final TextView coachAgeTextView;

        private final Button addButton;


        public FindCoachViewHolder(View itemView) {
            super(itemView);
            coachNameTextView = itemView.findViewById(R.id.coachNameTextView);
            coachAgeTextView = itemView.findViewById(R.id.coachAgeTextView);
            addButton = itemView.findViewById(R.id.addFriendButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener<UserDto> listener) {
            super.bind(user, listener);
            coachNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            coachAgeTextView.setText(32 + " years.");
            addButton.setOnClickListener(v -> addCoach(user));
        }


        private static void addCoach(UserDto user) {
            BackendService.addCoach(user, UserManager.getInstance().getId()).thenAccept(resultDto -> {
                if (resultDto != null) {
                    UserManager.getInstance().setCoachId(resultDto);
                }
            }).exceptionally(e -> {
                Log.d("myTag", "Find Coach Adapter: " + e.getMessage(), e);
                return null;
            });
        }
    }
}
