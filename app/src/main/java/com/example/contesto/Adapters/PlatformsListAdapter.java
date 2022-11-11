package com.example.contesto.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contesto.Activities.ShowContestActivity;
import com.example.contesto.R;
import com.example.contesto.Utils.Constants;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PlatformsListAdapter extends RecyclerView.Adapter<PlatformsListAdapter.PlatformsListAdapterViewHolder> {


    private List<String> ContestObjectArrayList;
    private Context context;

    public PlatformsListAdapter(Context context)
    {
        this.context = context;
    }

    public PlatformsListAdapter(Context context, List<String> ContestObjectArrayList)
    {
        this.context = context;
        this.ContestObjectArrayList = ContestObjectArrayList;
    }

    public void setData(List<String> data) {
        this.ContestObjectArrayList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlatformsListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_contest_title_main,parent,false);
        return new PlatformsListAdapterViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull PlatformsListAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //setting up Animation
        holder.contestButton.setAnimation(AnimationUtils.loadAnimation(context,R.anim.pop_in));
        holder.contestButton.setText(ContestObjectArrayList.get(position));

        holder.contestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ShowContestActivity.class).putExtra(Constants.WEBSITE,ContestObjectArrayList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount(){
        return ContestObjectArrayList.size();
    }

    public static class PlatformsListAdapterViewHolder extends RecyclerView.ViewHolder{
        private final MaterialButton contestButton;

        public PlatformsListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            contestButton = itemView.findViewById(R.id.contest_title);
        }
    }
}
