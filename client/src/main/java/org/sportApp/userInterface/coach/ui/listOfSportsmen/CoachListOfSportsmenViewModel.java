package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoachListOfSportsmenViewModel extends ViewModel {
    private final ArrayList<User> userList;
    private MutableLiveData<UserAdapter> mUserAdapter;
    private final static ArrayList<String> nameExamples =
            new ArrayList<>(Arrays.asList("Arina", "Diana", "Misha", "James", "Mary"));
    private final static ArrayList<String> surnameExamples =
            new ArrayList<>(Arrays.asList("I.", "Z.", "M.", "K.", "A."));

    public CoachListOfSportsmenViewModel() {
        userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 50; ++i) {
            userList.add(new User(nameExamples.get(random.nextInt(nameExamples.size()))
                    + " " + surnameExamples.get(random.nextInt(surnameExamples.size())), 1));
        }
    }

    public void setUserAdapterWithContext(android.content.Context context) {
        mUserAdapter = new MutableLiveData<>();
        mUserAdapter.setValue(new UserAdapter(context, userList));
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public MutableLiveData<UserAdapter> getUserAdapter() {
        return mUserAdapter;
    }
}
