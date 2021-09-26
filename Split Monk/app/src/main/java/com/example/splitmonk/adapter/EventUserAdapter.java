package com.example.splitmonk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitmonk.R;
import com.example.splitmonk.models.Event_User;
import com.example.splitmonk.models.Group;
import com.example.splitmonk.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class EventUserAdapter extends RecyclerView.Adapter<EventUserAdapter.ViewHolder> {

    Context context;
    Activity activity;
    ArrayList<User> data = new ArrayList<>();
    ArrayList<Event_User> selected_users = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();

    public EventUserAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.user_name.setText(data.get(position).getName());

        if(data.get(position).isSelected()){
            holder.root.setCardBackgroundColor(context.getColor(R.color.green));
            holder.add.setText("Remove");
            holder.add.setTextColor(Color.parseColor("#969898"));
        }else{
            holder.root.setCardBackgroundColor(Color.parseColor("#E9FFEDC0"));
            holder.add.setText("Add");
            holder.add.setTextColor(Color.parseColor("#17C261"));
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("this25", holder.user_amount.getText().toString());
                if(holder.user_amount.getText().toString().equals("")){
                    Toast.makeText(context, "Please enter some amount", Toast.LENGTH_SHORT).show();
                }else{
                    if(data.get(position).isSelected()){
                        data.get(position).setSelected(false);
                        map.remove(data.get(position).getUid());
                        holder.root.setCardBackgroundColor(Color.parseColor("#E9FFEDC0"));
                        holder.add.setText("Add");
                        holder.add.setTextColor(Color.parseColor("#17C261"));
                    }else {
                        data.get(position).setSelected(true);
                        map.put(data.get(position).getUid(), holder.user_amount.getText().toString());
                        holder.root.setCardBackgroundColor(context.getColor(R.color.green));
                        holder.add.setText("Remove");
                        holder.add.setTextColor(Color.parseColor("#969898"));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_amount;
        Button add;
        CardView root;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_amount = itemView.findViewById(R.id.user_amount);
            add = itemView.findViewById(R.id.btn_add);
            root = itemView.findViewById(R.id.card_root);
        }
    }

    public void setData(ArrayList<User> da){
        this.data = da;
        notifyDataSetChanged();
    }

    public ArrayList<Event_User> getSelected_users(){
        selected_users.clear();
        for(int i = 0; i < data.size(); i++){
            User curr_user = data.get(i);
            Log.e("this", map.getOrDefault(curr_user.getUid(), "-1"));
            if(!map.getOrDefault(curr_user.getUid(), "-1").equals("-1")) {
                Event_User curr = new Event_User(curr_user.getName(), curr_user.getUpi(), map.get(curr_user.getUid()),
                        "0", curr_user.getUid());
                selected_users.add(curr);
            }
        }

        return selected_users;
    }

}
