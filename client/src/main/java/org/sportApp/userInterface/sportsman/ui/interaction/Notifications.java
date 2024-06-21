package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;

import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.NotificationsAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseListDisplay;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends BaseListDisplay<String> {
    List<String> messages = new ArrayList<>();

    @Override
    protected BaseAdapter<String, ? extends BaseAdapter.BaseViewHolder<String>> createAdapter() {
        return new NotificationsAdapter(messages, new BaseAdapter.OnItemClickListener<String>() {
        });
    }


    @Override

    protected void getItems() {
        BackendService.getNotifications(UserManager.getInstance().getId()).thenAccept(resultDto -> {
            if (resultDto != null) {
                messages = resultDto;
            }
        }).exceptionally(e -> {
            Log.e("ApplicationTag", "Notifications: " + e.getMessage(), e);
            return null;
        }).join();
    }
}