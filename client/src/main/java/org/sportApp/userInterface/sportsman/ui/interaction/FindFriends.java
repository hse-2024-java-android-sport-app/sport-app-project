package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;
import android.widget.Toast;

import org.sportApp.requests.BackendService;


public class FindFriends extends FindUser {

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
