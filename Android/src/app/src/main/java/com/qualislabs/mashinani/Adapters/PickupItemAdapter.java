package com.qualislabs.mashinani.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qualislabs.mashinani.GoodsPickupActivity;
import com.qualislabs.mashinani.Interface.ItemClickListener;
import com.qualislabs.mashinani.Models.FarmerRequisition;
import com.qualislabs.mashinani.R;
import com.qualislabs.mashinani.ViewHolder.FarmerRequisitionViewHolder;

import java.util.List;

public class PickupItemAdapter extends RecyclerView.Adapter  {



    private Context context;
    private List<FarmerRequisition> pickupListItems;

    public PickupItemAdapter(Context context, List<FarmerRequisition> pickupListItems) {
        this.context = context;
        this.pickupListItems = pickupListItems;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pickup_item, parent, false);
        return new FarmerRequisitionViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final FarmerRequisition farmerRequisitionItem = pickupListItems.get(position);

        ((FarmerRequisitionViewHolder)holder).mTextViewRequisitionId.setText("MASN/PRID/0" + farmerRequisitionItem.getId());
        ((FarmerRequisitionViewHolder)holder).mTextViewProduceName.setText(farmerRequisitionItem.getProductType());
        ((FarmerRequisitionViewHolder)holder).mTextViewDate.setText(farmerRequisitionItem.getCreatedAt());


        ((FarmerRequisitionViewHolder)holder).setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent intent =  new Intent(context, GoodsPickupActivity.class);
                intent.putExtra("requisitionId", pickupListItems.get(position).getId() + "");
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return pickupListItems.size();
    }
}