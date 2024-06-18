package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;
import org.sportApp.utils.UserManager;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public static NavController navController;
    private List<UserDto> userList;
    private Context context;

    public UserAdapter(Context context, List<UserDto> userList) {
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
        UserDto user = userList.get(position);
        holder.nameTextView.setText(user.getFullName());
        holder.user = user;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public UserDto user;

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
                    //AccountViewModel.lastOpenedAccount = user;
                    UserManager.setLastUser(user);
                    navController.navigate(R.id.nav_account);
                }
            });
        }
    }
}
