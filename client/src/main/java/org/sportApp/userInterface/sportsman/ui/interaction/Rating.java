package org.sportApp.userInterface.sportsman.ui.interaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.RatingAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Rating extends BaseFragment<UserDto> {
    List<UserDto> friends = new ArrayList<>();
    @Override
    protected int getLayout() {
        return R.layout.fragment_rating;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.ratingRecyclerView;
    }

    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        return new RatingAdapter(friends, new BaseAdapter.OnItemClickListener<UserDto>() {
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getSortedFriends();
        super.onViewCreated(view, savedInstanceState);
    }


    private void getSortedFriends() {
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