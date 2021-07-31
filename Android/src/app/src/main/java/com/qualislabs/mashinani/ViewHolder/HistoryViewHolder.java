package com.qualislabs.mashinani.ViewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qualislabs.mashinani.Interface.ItemClickListener;
import com.qualislabs.mashinani.R;


public class HistoryViewHolder extends RecyclerView.ViewHolder implements ItemClickListener, View.OnClickListener {

    public TextView mProduceId, mProduceName, mDate;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HistoryViewHolder(View itemView) {
        super(itemView);

        mProduceId = (TextView) itemView.findViewById(R.id.txt_history_id);
        mProduceName = (TextView) itemView.findViewById(R.id.txt_produce_name);
        mDate = (TextView) itemView.findViewById(R.id.txt_history_date);

        itemView.setOnClickListener((View.OnClickListener) this);
    }


    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
