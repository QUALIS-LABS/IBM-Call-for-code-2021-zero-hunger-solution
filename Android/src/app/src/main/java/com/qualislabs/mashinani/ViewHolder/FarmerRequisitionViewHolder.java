package com.qualislabs.mashinani.ViewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qualislabs.mashinani.Interface.ItemClickListener;
import com.qualislabs.mashinani.R;


public class FarmerRequisitionViewHolder extends RecyclerView.ViewHolder implements ItemClickListener, View.OnClickListener {

    public TextView mTextViewRequisitionId, mTextViewProduceName, mTextViewDate;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FarmerRequisitionViewHolder(View itemView) {
        super(itemView);

        mTextViewRequisitionId = (TextView) itemView.findViewById(R.id.txt_pickup_requisition_id);
        mTextViewProduceName = (TextView) itemView.findViewById(R.id.txt_pickup_produce_name);
        mTextViewDate = (TextView) itemView.findViewById(R.id.txt_pickup_date);

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
