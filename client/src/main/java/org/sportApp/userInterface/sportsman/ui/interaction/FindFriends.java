package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;
import android.widget.Toast;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;


public class FindFriends extends FindUser {

    @Override
    protected void searchUsers(String userName) {
        {
            BackendService.searchSportsman(userName).thenAccept(resultDto -> {
                if (resultDto != null) {
                    users.clear();
                    users.addAll(resultDto); // maybe doesn't work, in this case should implement method for getting users
                    Log.d("myTag", users.toString());
                }
            }).exceptionally(e -> {
                Log.d("SearchWindow", "Search failed: " + e.getMessage(), e);
                return null;
            }).join();
        }
    }
}
