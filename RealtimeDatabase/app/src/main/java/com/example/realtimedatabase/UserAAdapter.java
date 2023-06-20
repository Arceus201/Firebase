package com.example.realtimedatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAAdapter extends  RecyclerView.Adapter<UserAAdapter.UserViewHolder>{
    private List<User1> mListUser;

    public UserAAdapter(List<User1> mListUser) {
        this.mListUser = mListUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User1 user1 = mListUser.get(position);
        if(user1 == null) return ;

        holder.id.setText("ID: " + user1.getId());
        holder.name.setText("Name: " + user1.getName());
    }

    @Override
    public int getItemCount() {
        if(mListUser!=null) return mListUser.size();

        return 0;
    }

    public  class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView id,name;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tv_id);
            name = itemView.findViewById(R.id.tv_name);
        }
    }
}
