package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;
import android.widget.Toast;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.FindUserAdapter;


public class FindFriends extends FindUser {

    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        return new FindUserAdapter(users, new BaseAdapter.OnItemClickListener<UserDto>() {
        });
    }

    @Override
    protected void searchUsers(String userName) {
        BackendService.searchFriends(userName).thenAccept(resultDto -> {
            if (resultDto != null) {
                super.users = resultDto; // maybe doesn't work, in this case should implement method for getting users
            } else {
                Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
            }
        }).exceptionally(e -> {
            Log.d("SearchWindow", "Search failed: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
