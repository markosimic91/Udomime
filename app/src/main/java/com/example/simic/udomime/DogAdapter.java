package com.example.simic.udomime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simic on 26.10.2017..
 */

public class DogAdapter extends BaseAdapter {
    private ArrayList<Dog> mDogList;

    public DogAdapter(ArrayList<Dog> mDogList) {
        this.mDogList = mDogList;
        this.mDogList.addAll(mDogList);
    }

    @Override
    public int getCount() {
        return this.mDogList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mDogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnimalViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dog_list_item,parent,false);
            holder = new AnimalViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (AnimalViewHolder) convertView.getTag();
        }

        Dog dog = this.mDogList.get(position);

        Picasso.with(parent.getContext())
                .load(dog.getmDogPicure())
                .fit()
                .centerCrop()
                .into(holder.ivDogPic);

        holder.tvDogContact.setText(dog.getmDogContact());
        holder.tvDogName.setText(dog.getmDogName());
        holder.tvDogDescription.setText(dog.getmDogDescription());

        return convertView;
    }

    static class AnimalViewHolder {
        @BindView(R.id.ivDogPic)
        ImageView ivDogPic;
        @BindView(R.id.tvDogName)
        TextView tvDogName;
        @BindView(R.id.tvDogDescription)
        TextView tvDogDescription;
        @BindView(R.id.tvDogContact)
        TextView tvDogContact;

        public AnimalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
