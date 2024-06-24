package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.UserDto;
import org.sportApp.model.Notifications;
import org.sportApp.userInterface.R;

import java.util.List;

public class NotificationsAdapter extends BaseAdapter<Notifications, BaseAdapter.BaseViewHolder<Notifications>> {
    public NotificationsAdapter(List<Notifications> items, OnItemClickListener<Notifications> listener) {
        super(items, R.layout.item_notifications, listener, NotificationsViewHolder::new);
    }

    public static class NotificationsViewHolder extends BaseViewHolder<Notifications> {
        private final TextView messageTextView;
        private final TextView userTextView;
        public NotificationsViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            userTextView = itemView.findViewById(R.id.userTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull Notifications notification, OnItemClickListener<Notifications> listener) {
            super.bind(notification, listener);
            messageTextView.setText(notification.getMessage());
            userTextView.setText(notification.getUser());
        }

    }
}
