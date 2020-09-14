package com.redbravo.inventorylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ItemViewHolder> {
    private Context mContext;
    private List<Sales> mUploads;


    public SalesAdapter(Context mContext, List<Sales> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.saleslist, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Sales sales = mUploads.get(position);
        holder.itemname.setText(sales.getItemname());
        holder.itemprice.setText(String.valueOf(sales.getItemprice()));
        holder.itemquant.setText(String.valueOf(sales.getSolditems()));
        holder.date.setText(String.valueOf(sales.getSaledate()));
        holder.total.setText(String.valueOf(sales.getTotalgained()));
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemname, itemprice, itemquant, date, total;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.itname);
            itemprice = itemView.findViewById(R.id.unitprice);
            itemquant = itemView.findViewById(R.id.soldno);
            date = itemView.findViewById(R.id.datesold);
            total = itemView.findViewById(R.id.total);


        }


    }
}