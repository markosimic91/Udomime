package com.example.simic.udomime;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simic on 20.10.2017..
 */

public class CatFragment extends Fragment {

    public static final String TITLE = "Mačka";
    private static final String CAT = "Cat";
    private DatabaseReference mCatReference;
    private RecyclerView mCatList;
    private View view;



    public CatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cat_fragment, container, false);

        mCatList = (RecyclerView) view.findViewById(R.id.rlvCats);
        mCatReference = FirebaseDatabase.getInstance().getReference().child(CAT);
        mCatList.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Cat,CatAdapter.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cat,CatAdapter.ViewHolder>(
                Cat.class,
                R.layout.cat_item_list,
                CatAdapter.ViewHolder.class,
                mCatReference

        ) {
            @Override
            protected void populateViewHolder(CatAdapter.ViewHolder viewHolder, Cat model, int position) {
                viewHolder.tvCatName.setText(model.getmCatName());
                viewHolder.tvCatDescription.setText(model.getmCatDescription());
                viewHolder.tvCatContact.setText(model.getmCatContact());
                Picasso.with(getContext()).load(model.getmCatPicure()).into(viewHolder.ivCatPic);
            }

        };

        mCatList.setAdapter(firebaseRecyclerAdapter);
    }

}


