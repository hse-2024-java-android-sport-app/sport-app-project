package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.RatingAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseListDisplay;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Rating extends BaseListDisplay<UserDto> {
    List<UserDto> friends = new ArrayList<>();
    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        return new RatingAdapter(friends, new BaseAdapter.OnItemClickListener<UserDto>() {
        });
    }

    @Override
    protected void getItems() {
        BackendService.getSubscriptions(UserManager.getInstance().getId()).thenAccept(resultDto -> {
            if (resultDto != null) {
                friends = resultDto;
            }
        }).exceptionally(e -> {
            Log.e("ApplicationTag", "Rating: " + e.getMessage(), e);
            return null;
        }).join();
    }
}