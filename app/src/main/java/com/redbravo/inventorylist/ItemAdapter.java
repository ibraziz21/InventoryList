package com.redbravo.inventorylist;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
private Context mContext;
private List<uploadClass> mUploads;

public  ItemAdapter(Context context,List<uploadClass>uploads){
    mContext = context;
    mUploads = uploads;

}
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemlist, parent, false);
        return new ItemViewHolder(v);
}

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
    uploadClass uploadCurrent = mUploads.get(position);
    holder.itemname.setText(uploadCurrent.getProductname());
    holder.itemsku.setText(String.valueOf(uploadCurrent.getSku()));
    holder.itemquant.setText(String.valueOf(uploadCurrent.getQuantity()));
    holder.itemprice.setText(String.valueOf(uploadCurrent.getPrice()));
        Picasso.with(mContext)
                .load(uploadCurrent.getImageuri())
                .fit()
                .into(holder.itemimage);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemname, itemprice, itemquant, itemsku;
        ImageView itemimage;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.productnme);
            itemprice = itemView.findViewById(R.id.itemprice);
            itemquant = itemView.findViewById(R.id.itemstock);
            itemsku = itemView.findViewById(R.id.itemsku);
            itemimage = itemView.findViewById(R.id.itemimg);

        }
    }

}
