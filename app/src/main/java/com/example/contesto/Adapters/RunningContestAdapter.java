package com.example.contesto.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contesto.Models.ContestObject;
import com.example.contesto.R;
import com.example.contesto.Utils.Methods;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunningContestAdapter extends RecyclerView.Adapter<RunningContestAdapter.RunningContestAdapterViewHolder> {

private String[] dark ={"#ff2bc8d9","#ffff9b2b","#ff948bfe","#fffe6d6e"};
private String[] light={"#ffd9f5f8","#ffffedd9","#ffeceaff","#ffffe5e6"};

private List<ContestObject> ContestObjectArrayList;
private List<ContestObject> DummyContestObjectArrayList;
private Context context;
private List<Boolean> CheckMoreFlag;
private Filter FilteredData = new Filter() {
@Override
protected FilterResults performFiltering(CharSequence constraint) {
        List<ContestObject> filtered = new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
        filtered.addAll(DummyContestObjectArrayList);
        } else {
        String check = constraint.toString().toLowerCase().trim();

        for (ContestObject items : DummyContestObjectArrayList) {
        if (items.getTitle().toLowerCase().contains(check) || items.getPlatform().toLowerCase().contains(check)
        || items.getStatus().toLowerCase().contains(check)) {
        filtered.add(items);
        }
        }
        }

        FilterResults results = new FilterResults();
        results.values = filtered;

        return results;
        }

@Override
protected void publishResults(CharSequence constraint, FilterResults results) {
        ContestObjectArrayList.clear();
        ContestObjectArrayList.addAll((List) results.values);
        notifyDataSetChanged();
        }
        };


public RunningContestAdapter(Context context) {
        this.context = context;
        }

public RunningContestAdapter(Context context, List<ContestObject> ContestObjectArrayList) {
        this.context = context;
        this.ContestObjectArrayList = ContestObjectArrayList;
        InitializeBool();
        }

public void setData(List<ContestObject> data) {
        this.ContestObjectArrayList = data;
        notifyDataSetChanged();
        InitializeBool();
        }

public void InitializeBool() {
        DummyContestObjectArrayList = new ArrayList<>(ContestObjectArrayList);
        CheckMoreFlag = new ArrayList<Boolean>(getItemCount());
        CheckMoreFlag.addAll(Collections.nCopies(getItemCount(), Boolean.FALSE));
        }



@NonNull
@Override
public RunningContestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_running_card, parent, false);
        return new RunningContestAdapterViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull RunningContestAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.viewHolder.setBackgroundColor(Color.parseColor(light[position%4]));
        holder.mRoundName.setTextColor(Color.parseColor(dark[position%4]));
        holder.mCard.setAnimation(AnimationUtils.loadAnimation(context,R.anim.pop_in));


        holder.mCard.setAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_in));

        holder.mRoundName.setText(ContestObjectArrayList.get(position).getTitle());
        holder.mDateEnd.setText(Methods.getStringFormat(ContestObjectArrayList.get(position).getEnd()));
        holder.mDateStart.setText(Methods.getStringFormat(ContestObjectArrayList.get(position).getStart()));
        holder.mDuration.setText(durationFormater(ContestObjectArrayList.get(position).getDuration()));
        holder.platformName.setText(ContestObjectArrayList.get(position).getPlatform());

        holder.share.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
//              // implemented share of contest
        // getStart() provides ie. 2014-07-07T18:30:00.000Z
        String startDate = ContestObjectArrayList.get(position).getStart().substring(0, 16);
        String endDate = ContestObjectArrayList.get(position).getEnd().substring(0, 16);

        String formatStart = startDate.substring(8, 10)+"-"+startDate.substring(5,7)+"-"+startDate.substring(0,4)+" ("+startDate.substring(11,16)+")";
        String formatEnd = endDate.substring(8, 10)+"-"+endDate.substring(5,7)+"-"+endDate.substring(0,4)+" ("+endDate.substring(11,16)+")";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String message = "Checkout this contest on " +
        ContestObjectArrayList.get(position).getPlatform() + " \n" +
        ContestObjectArrayList.get(position).getTitle() +
        "\nDuration " + Double.parseDouble(ContestObjectArrayList.get(position).getDuration())/3600 +" hrs" +
        "\nStatus " + ContestObjectArrayList.get(position).getStatus() +
        "\nStart Time " + formatStart +
        "\nEnd Time " + formatEnd + "\n" +
        ContestObjectArrayList.get(position).getLink();
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);

        }
        });
        holder.mCard.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
//                Toast.makeText(context, "Implement Notification " + position, Toast.LENGTH_SHORT).show();
        String url = ContestObjectArrayList.get(position).getLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
        }
        });
        holder.calendarReminder.setOnClickListener(new View.OnClickListener() {
@RequiresApi(api = Build.VERSION_CODES.O)
@Override
public void onClick(View view) {
//               Toast.makeText(context,ContestObjectArrayList.get(position).toString(),Toast.LENGTH_SHORT).show();

        // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        LocalDateTime start = null;
        start = LocalDateTime.parse(ContestObjectArrayList.get(position).getStart().substring(0, ContestObjectArrayList.get(position).getStart().length() - 2));
        LocalDateTime stop = LocalDateTime.parse(ContestObjectArrayList.get(position).getEnd().substring(0, ContestObjectArrayList.get(position).getEnd().length() - 2));

        Intent intent = new Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, ContestObjectArrayList.get(position).getTitle())
        .putExtra(CalendarContract.Events.EVENT_LOCATION, ContestObjectArrayList.get(position).getPlatform())
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, stop.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        context.startActivity(intent);


        }
        });

        }

private String durationFormater(String duration){
        String formatDuration = "";

        double doubleDuration = Double.parseDouble(duration);
        long durMin = (long)doubleDuration / 60;

        // contests of more than a year
        if(durMin > 518400){
        formatDuration += "For practice";
        return formatDuration;
        }

        // contests of more than a month
        if(durMin > 43200){
        formatDuration += durMin/43200 +" month";
        return formatDuration;
        }

        // There are 24 * 60 minutes in a day
        if(durMin > 1440){
        formatDuration += durMin/1440 +" days";
        durMin %= 1440;

        if(durMin >0){
        formatDuration +=", ";
        }
        }

        if(durMin > 0){
        formatDuration += durMin/60 +" hr";
        durMin %= 60;
        }

        if(durMin > 0){
        formatDuration += ", ";
        formatDuration += durMin + " min";
        }

        // format Duration is in format ie "3 days, 4 hr, 30 min"
        return formatDuration;
        }

@Override
public int getItemCount() {

        return ContestObjectArrayList.size();
        }

public class RunningContestAdapterViewHolder extends RecyclerView.ViewHolder {

    private final TextView mRoundName;
    private final TextView mDateStart;
    private final TextView mDateEnd, mDuration;
    private final CardView mCard;
    private final Button platformName;
    private final Button calendarReminder, share;
    LinearLayout viewHolder;
    public RunningContestAdapterViewHolder(@NonNull View itemView) {
        super(itemView);

        mRoundName = itemView.findViewById(R.id.tv_round_name);
        mDateStart = itemView.findViewById(R.id.date_start);
        mDateEnd = itemView.findViewById(R.id.date_end);
        mCard = itemView.findViewById(R.id.card);
        platformName = itemView.findViewById(R.id.platform_name);
        calendarReminder = itemView.findViewById(R.id.set_reminder);
        share = itemView.findViewById(R.id.share_in_whatsapp);
        mDuration = itemView.findViewById(R.id.duration);
        viewHolder=itemView.findViewById(R.id.contest_detail_view_holder);
    }
}
}
