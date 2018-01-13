package com.example.simic.udomime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Simic on 20.10.2017..
 */

public class DogFragment extends Fragment {

    public static final String TITLE = "Dog";
    private static final String DOG = "Dog";
    private DatabaseReference mDogReference;
    private RecyclerView mDogList;
    private View view;



    public DogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dog_fragment, container, false);

        mDogList = (RecyclerView) view.findViewById(R.id.rlvDogs);
        mDogReference = FirebaseDatabase.getInstance().getReference().child(DOG);
        mDogList.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Dog,DogAdapter.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Dog, DogAdapter.ViewHolder>(
                Dog.class,
                R.layout.dog_item_list,
                DogAdapter.ViewHolder.class,
                mDogReference

        ) {
            @Override
            protected void populateViewHolder(DogAdapter.ViewHolder viewHolder, Dog model, final int position) {

                final String dog_key = getRef(position).getKey();

                viewHolder.tvDogName.setText(model.getmDogName());
                viewHolder.tvDogDesription.setText(model.getmDogDescription());
                viewHolder.tvDogContact.setText(model.getmDogContact());
                Picasso.with(getContext()).load(model.getmDogPicture()).into(viewHolder.ivDogPic);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleDog = new Intent(getActivity(),SingleDog.class);
                        singleDog.putExtra("dog_id",dog_key);
                        startActivity(singleDog);


                    }
                });
                    viewHolder.ibLeaveComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent comment = new Intent(getActivity(),Chat.class);
                            comment.putExtra("dog_comment",dog_key);
                            startActivity(comment);
                    }
                });

                
            }
        };

        mDogList.setAdapter(firebaseRecyclerAdapter);

    }

}


