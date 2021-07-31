package com.qualislabs.mashinani.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qualislabs.mashinani.Interface.ItemClickListener;
import com.qualislabs.mashinani.R;
import com.qualislabs.mashinani.Models.History;
import com.qualislabs.mashinani.ViewHolder.HistoryViewHolder;

import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter  {



    private Context context;
    private List<History> historyListItems;

    public HistoryItemAdapter(Context context, List<History> historyListItems) {
        this.context = context;
        this.historyListItems = historyListItems;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final History historyItem = historyListItems.get(position);

        ((HistoryViewHolder)holder).mProduceId.setText("MASN/PRID/0" + historyItem.getProduceId());
        ((HistoryViewHolder)holder).mProduceName.setText(historyItem.getProduceName());
        ((HistoryViewHolder)holder).mDate.setText(historyItem.getDate());


        ((HistoryViewHolder)holder).setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

//                Intent intent =  new Intent(context, MoreDetailsActivity.class);
//                intent.putExtra("historyId", historyListItems.get(position).getProduceId() + "");
//                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return historyListItems.size();
    }
}