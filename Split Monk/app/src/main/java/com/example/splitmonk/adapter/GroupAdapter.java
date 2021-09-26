package com.example.splitmonk.adapter;

import static java.util.Collections.min;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitmonk.Activities.GroupDashboardActivity;
import com.example.splitmonk.R;
import com.example.splitmonk.models.Group;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{

    Context context;
    Activity activity;
    ArrayList<Group> data = new ArrayList<>();

    public GroupAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Group curr = data.get(position);
        holder.name.setText(curr.getGroup_name());
        holder.group_desc.setText(curr.getGroup_desc());

        if(curr.getUsers().size() > 3){
            holder.group_more.setVisibility(View.VISIBLE);
            holder.group_more.setText("+" + String.valueOf(curr.getUsers().size() - 3) + " more");
        }else{
            holder.group_more.setVisibility(View.GONE);
        }

        if(curr.getUsers().size() == 1){
            holder.av1.setImageResource(R.drawable.avtar_1);
            holder.av3.setVisibility(View.GONE);
            holder.av2.setVisibility(View.GONE);
        }else if(curr.getUsers().size() == 2){
            holder.av1.setImageResource(R.drawable.avtart_3);
            holder.av2.setImageResource(R.drawable.avtar_2);
            holder.av3.setVisibility(View.GONE);
        }else{
            holder.av1.setImageResource(R.drawable.avtar_2);
            holder.av2.setImageResource(R.drawable.avtar_1);
            holder.av3.setImageResource(R.drawable.avtart_3);
        }

        holder.group_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("group_id", curr.getGroup_id());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, "Group ID has been copied to your clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(activity, GroupDashboardActivity.class);
                i.putExtra("group_id", curr.getGroup_id());
                context.startActivity(i);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, group_desc, group_code;
        ImageView av1, av2, av3;
        TextView group_more;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.group_name);
            group_desc = itemView.findViewById(R.id.group_desc);
            group_code = itemView.findViewById(R.id.group_code);
            group_more = itemView.findViewById(R.id.group_more);
            av1 = itemView.findViewById(R.id.avatar_1);
            av2 = itemView.findViewById(R.id.avatar_2);
            av3 = itemView.findViewById(R.id.avatar_3);

        }
    }

    public void setData(ArrayList<Group> da){
        this.data = da;
        notifyDataSetChanged();
    }
}
