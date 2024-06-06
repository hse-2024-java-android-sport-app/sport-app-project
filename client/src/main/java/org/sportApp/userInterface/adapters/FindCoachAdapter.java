package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.registration.UserDto;
import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class FindCoachAdapter extends RecyclerView.Adapter<FindCoachAdapter.FindCoachViewHolder> {
    private final List<UserDto> coachs;
    private final OnItemClickListener listener;

    public FindCoachAdapter(List<UserDto> coachsList, OnItemClickListener listener) {
        this.coachs = coachsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FindCoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_coach, parent, false);
        return new FindCoachViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindCoachViewHolder holder, int position) {
        UserDto coach = coachs.get(position);
        holder.bind(coach, listener);
    }

    @Override
    public int getItemCount() {
        return coachs.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }

    public static class FindCoachViewHolder extends RecyclerView.ViewHolder {
        private final TextView coachNameTextView;
        private final TextView coachAgeTextView;

        public FindCoachViewHolder(View itemView) {
            super(itemView);
            coachNameTextView = itemView.findViewById(R.id.coachNameTextView);
            coachAgeTextView = itemView.findViewById(R.id.coachAgeTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull UserDto user, OnItemClickListener listener) {
            coachNameTextView.setText(user.getFirstName() + " " + user.getSecondName());
            coachAgeTextView.setText(32 + " years.");
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(getAdapterPosition());
                }
                return true;
            });
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
