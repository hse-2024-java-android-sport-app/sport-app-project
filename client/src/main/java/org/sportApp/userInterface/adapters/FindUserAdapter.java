package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.registration.RegistrationWindow;
import org.sportApp.utils.UserManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FindUserAdapter extends BaseAdapter<UserDto, BaseAdapter.BaseViewHolder<UserDto>> {

    public FindUserAdapter(List<UserDto> items, OnItemClickListener<UserDto> listener) {
        super(items, R.layout.item_find_coach, listener, FindUserViewHolder::new);
    }

    public static class FindUserViewHolder extends BaseViewHolder<UserDto> {
        private final TextView userNameTextView;
        private final TextView userAgeTextView;

        private final Button addButton;


        public FindUserViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.coachNameTextView);
            userAgeTextView = itemView.findViewById(R.id.coachAgeTextView);
            addButton = itemView.findViewById(R.id.addFriendButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener<UserDto> listener) {
            super.bind(user, listener);
            userNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            userAgeTextView.setText(user.getAge() + " years.");
            addButton.setOnClickListener(v ->
            {
                if (user.getType() == UserDto.Kind.coach) {
                    Boolean res = getIsCoachSet(UserManager.getInstance());
                    if (res == Boolean.TRUE) {
                        Toast.makeText(v.getContext(), "Your trainer exists, are you sure you want to overwrite?", Toast.LENGTH_SHORT).show();
                    }
                }
                addUser(user);
                Toast.makeText(v.getContext(), "The subscription request has been sent", Toast.LENGTH_SHORT).show();
            });
        }

        private static void addUser(@NonNull UserDto user) {
            Log.d("myTag", user.getId() + " " + UserManager.getInstance().getId());
            if (user.getType().equals(UserDto.Kind.coach)) {
                BackendService.editCoach(UserManager.getInstance().getId(), user.getId()).thenAccept(resultDto -> {
                    if (resultDto != null) {
                        UserManager.getInstance().setCoachId(resultDto);
                    }
                }).exceptionally(e -> {
                    Log.d("myTag", "Find Coach Adapter: " + e.getMessage(), e);
                    return null;
                });
            } else {
                BackendService.addSubscription(UserManager.getInstance().getId(), user.getId()).thenAccept(resultDto -> {
                }).exceptionally(e -> {
                    Log.d("myTag", "Find Coach Adapter: " + e.getMessage(), e);
                    return null;
                });
            }
        }
    }

    private static Boolean getIsCoachSet(@NonNull UserDto user) {
        try {
            return BackendService.getIsCoachSet(user.getId()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("ApplicationTag", "FindUserAdapter " + e.getMessage(), e);
        }
        return null;
    }
}
