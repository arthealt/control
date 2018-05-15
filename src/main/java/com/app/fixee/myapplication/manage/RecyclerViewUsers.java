package com.app.fixee.myapplication.manage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseClickUser;
import com.app.fixee.myapplication.models.User;

import java.util.List;

public class RecyclerViewUsers extends RecyclerView.Adapter<RecyclerViewUsers.UserViewHolder> {

    private List<User> users;
    private OnResponseClickUser callback;

    public RecyclerViewUsers(List<User> users, OnResponseClickUser callback) {
        this.users = users;
        this.callback = callback;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.accessLevel.setText(users.get(position).getAccessLevel());

        holder.linearLayout.setOnClickListener(view -> callback.onSuccess(users.get(position).getName(), users.get(position).getDocId(), position));
    }

    @Override
    public int getItemCount() {
        return users.isEmpty() ? 0 : users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name;
        TextView accessLevel;

        public UserViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            name = itemView.findViewById(R.id.name);
            accessLevel = itemView.findViewById(R.id.access_level);
        }
    }
}
