package org.sportApp.userInterface.coach.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.sportApp.userInterface.databinding.FragmentHomeBinding;
import org.sportApp.userInterface.databinding.FragmentMainAccountBinding;
import org.sportApp.userInterface.coach.ui.home.HomeViewModel;

public class MainAccountFragment extends Fragment {

    private FragmentMainAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainAccountViewModel mainAccountViewModel =
                new ViewModelProvider(this).get(MainAccountViewModel.class);

        binding = FragmentMainAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView userNameView = binding.userName;
        mainAccountViewModel.getUserName().observe(getViewLifecycleOwner(), userNameView::setText);
        final TextView userTypeView = binding.userType;
        mainAccountViewModel.getUserType().observe(getViewLifecycleOwner(), userTypeView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
