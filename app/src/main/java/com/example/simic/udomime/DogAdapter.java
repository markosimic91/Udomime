package com.example.simic.udomime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simic on 29.11.2017..
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder>{

    ArrayList<Dog> mDogs;

    public DogAdapter(ArrayList<Dog> mDogs) {
        this.mDogs = mDogs;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dogView = inflater.inflate(R.layout.dog_item_list,parent,false);
        ViewHolder dogViewHolder = new ViewHolder(dogView);
        return dogViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dog dog = this.mDogs.get(position);

        holder.tvDogName.setText(dog.getmDogName());
        holder.tvDogContact.setText(dog.getmDogContact());
        holder.tvDogDesription.setText(dog.getmDogDescription());

        Picasso.with(holder.tvDogName.getContext())
                .load(dog.getmDogPicure())
                .fit()
                .centerCrop()
                .into(holder.ivDogPic);

    }

    @Override
    public int getItemCount() {
        return this.mDogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.tvDogName) TextView tvDogName;
        @BindView(R.id.tvDogDescription) TextView tvDogDesription;
        @BindView(R.id.tvDogContact) TextView tvDogContact;
        @BindView(R.id.ivDogPic) ImageView ivDogPic;
        @BindView(R.id.bAdopted) Button bAdopted;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

