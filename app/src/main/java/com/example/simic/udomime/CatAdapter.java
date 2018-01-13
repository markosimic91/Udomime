package com.example.simic.udomime;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simic on 29.11.2017..
 */

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder>{

    ArrayList<Cat> mCats;

    public CatAdapter(ArrayList<Cat> mCats) {

        this.mCats = mCats;

    }

    @Override
    public CatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View catView = inflater.inflate(R.layout.cat_item_list,parent,false);
        ViewHolder catViewHolder = new ViewHolder(catView);
        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(CatAdapter.ViewHolder holder, int position) {
        Cat cat = this.mCats.get(position);

        holder.tvCatContact.setText(cat.getmCatContact());
        holder.tvCatDescription.setText(cat.getmCatContact());
        holder.tvCatName.setText(cat.getmCatName());

        Picasso.with(holder.tvCatName.getContext())
                .load(cat.getmCatPicture())
                .fit()
                .centerCrop()
                .into(holder.ivCatPic);
    }

    @Override
    public int getItemCount() {
        return this.mCats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvCatName) TextView tvCatName;
        @BindView(R.id.tvCatContact) TextView tvCatContact;
        @BindView(R.id.tvCatDescription) TextView tvCatDescription;
        @BindView(R.id.ivCatPic) ImageView ivCatPic;
        @BindView(R.id.ibLeaveComment) ImageButton ibLeaveComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
