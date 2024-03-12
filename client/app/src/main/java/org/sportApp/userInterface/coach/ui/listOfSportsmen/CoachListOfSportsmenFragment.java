package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.sportApp.userInterface.coach.databinding.FragmentCoachListOfSportsmenBinding;

public class CoachListOfSportsmenFragment extends Fragment {

    private FragmentCoachListOfSportsmenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CoachListOfSportsmenViewModel coachListOfSportsmenViewModel =
                new ViewModelProvider(this).get(CoachListOfSportsmenViewModel.class);

        binding = FragmentCoachListOfSportsmenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCoachListOfSportsmen;
        coachListOfSportsmenViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}