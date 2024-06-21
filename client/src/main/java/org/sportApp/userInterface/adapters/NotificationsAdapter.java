package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.userInterface.R;

import java.util.List;

public class NotificationsAdapter extends BaseAdapter<String, BaseAdapter.BaseViewHolder<String>> {
    public NotificationsAdapter(List<String> items, OnItemClickListener<String> listener) {
        super(items, R.layout.item_notifications, listener, NotificationsViewHolder::new);
    }

    public static class NotificationsViewHolder extends BaseViewHolder<String> {
        private final TextView messageTextView;
        public NotificationsViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull String message, OnItemClickListener<String> listener) {
            super.bind(message, listener);
            messageTextView.setText(message);
        }

    }
}
