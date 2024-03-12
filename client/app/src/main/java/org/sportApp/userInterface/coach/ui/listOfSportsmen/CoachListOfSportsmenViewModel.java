package org.sportApp.userInterface.coach.ui.listOfSportsmen;

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
    private ArrayList<User> userList;

    private final static ArrayList<String> nameExamples =
            new ArrayList<>(Arrays.asList("Arina", "Diana", "Misha", "James", "Mary"));
    private final static ArrayList<String> surnameExamples =
            new ArrayList<>(Arrays.asList("Ivanova", "Zalilova", "Minaev", "Ivanov", "Kim"));

    public CoachListOfSportsmenViewModel() {
        userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            userList.add(new User(nameExamples.get(random.nextInt()) + " " +
                    surnameExamples.get(random.nextInt()), 1));
        }
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}
