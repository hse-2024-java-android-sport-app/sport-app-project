package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.model.User;
import org.sportApp.userInterface.databinding.FragmentCoachListOfSportsmenBinding;

import java.util.ArrayList;
import java.util.List;

public class CoachListOfSportsmenFragment extends Fragment {

    private FragmentCoachListOfSportsmenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CoachListOfSportsmenViewModel coachListOfSportsmenViewModel =
                new ViewModelProvider(this).get(CoachListOfSportsmenViewModel.class);
        coachListOfSportsmenViewModel.setUserAdapterWithContext(this.getContext());

        binding = FragmentCoachListOfSportsmenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final RecyclerView recyclerView = binding.userRecyclerView;
        coachListOfSportsmenViewModel.getUserAdapter().observe(getViewLifecycleOwner(),
                recyclerView::setAdapter);
        UserAdapter.navController = NavHostFragment.findNavController(this);
        //binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //binding.userRecyclerView.setAdapter(
                //new UserAdapter(this.getContext(), coachListOfSportsmenViewModel.getUserList()));
                //new UserAdapter(this.getContext(), new ArrayList<>()));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}