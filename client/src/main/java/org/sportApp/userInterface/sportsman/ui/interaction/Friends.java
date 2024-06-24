package org.sportApp.userInterface.sportsman.ui.interaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.FriendAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Friends extends BaseFragment<UserDto> {
    private int typeOfButton;
    List<UserDto> subscribers = new ArrayList<>();
    List<UserDto> subscriptions = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.recyclerViewFriends;
    }

    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        if (typeOfButton == 0) {
            return new FriendAdapter(subscribers, new BaseAdapter.OnItemClickListener<UserDto>() {
            });
        } else {
            return new FriendAdapter(subscriptions, new BaseAdapter.OnItemClickListener<UserDto>() {
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonSubscribers = view.findViewById(R.id.buttonSubscribers);
        buttonSubscribers.setOnClickListener(v -> {
            typeOfButton = 0;
            getFriends(typeOfButton);
            super.onViewCreated(view, savedInstanceState);
        });

        Button buttonSubscriptions = view.findViewById(R.id.buttonSubscriptions);
        buttonSubscriptions.setOnClickListener(v -> {
            typeOfButton = 1;
            getFriends(typeOfButton);
            super.onViewCreated(view, savedInstanceState);
        });
    }


    private void getFriends(int type) {
        if (type == 0) {
            BackendService.getFollowers(UserManager.getInstance().getId()).thenAccept(resultDto -> {
                if (resultDto != null) {
                    subscribers = resultDto;
                } else {
                    Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
                }
            }).exceptionally(e -> null);
        } else {
            BackendService.getSubscriptions(UserManager.getInstance().getId()).thenAccept(resultDto -> {
                if (resultDto != null) {
                    subscriptions = resultDto;
                } else {
                    Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
                }
            }).exceptionally(e -> null);
        }
    }
}
