package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;

import org.sportApp.requests.BackendService;


public class FindFriends extends FindUser {

    @Override
    protected void searchUsers(String userName) {
        {
            BackendService.searchSportsman(userName).thenAccept(resultDto -> {
                if (resultDto != null) {
                    users.clear();
                    users.addAll(resultDto);
                    Log.d("ApplicationTag", "FindFriendsWindow: users " + users.toString());
                }
            }).exceptionally(e -> {
                Log.e("ApplicationTag", "FindFriends: search failed " + e.getMessage(), e);
                return null;
            }).join();
        }
    }
}
