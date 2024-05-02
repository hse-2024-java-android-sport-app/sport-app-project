package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import org.sportApp.model.User;
import org.sportApp.userInterface.coach.MainActivity;
import org.sportApp.userInterface.coach.R;
import org.sportApp.userInterface.coach.ui.account.AccountFragment;
import org.sportApp.userInterface.coach.ui.account.AccountViewModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public static NavController navController;
    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.user = user;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public User user;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_name);
            /*nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof AppCompatActivity) {
                        Toast.makeText(context, nameTextView.getText(), Toast.LENGTH_LONG).show();
                        AccountViewModel.lastOpenedAccount = user;
                        AccountFragment fragment = new AccountFragment();
                        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        //transaction.remove(manager.findFragmentById(R.id.sportlist));
                        transaction.replace(container.getId(),fragment);
                        transaction.commit();
                    } else {
                        Toast.makeText(context, nameTextView.getText(), Toast.LENGTH_LONG).show();
                    }
                }
            });*/
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountViewModel.lastOpenedAccount = user;
                    navController.navigate(R.id.nav_account);
                }
            });
        }
    }
}
