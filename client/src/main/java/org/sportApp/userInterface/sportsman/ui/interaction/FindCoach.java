package org.sportApp.userInterface.sportsman.ui.interaction;

import android.util.Log;

import org.sportApp.requests.BackendService;


public class FindCoach extends FindUser {

    @Override
    protected void searchUsers(String userName) {
        {
            BackendService.searchCoaches(userName).thenAccept(resultDto -> {
                if (resultDto != null) {
                    users.clear();
                    users.addAll(resultDto);
                    Log.d("ApplicationTag", "FindCoachWindow: users " + users.toString());
                }
            }).exceptionally(e -> {
                Log.e("ApplicationTag", "FindCoachWindow: search failed " + e.getMessage(), e);
                return null;
            }).join();
        }
    }
}
