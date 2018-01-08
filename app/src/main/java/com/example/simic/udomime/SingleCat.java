package com.example.simic.udomime;

import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleCat extends AppCompatActivity {

    private static final String CAT = "Cat";
    private String mCat_key = null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @BindView(R.id.ivCatPic) ImageView ivCatPic;
    @BindView(R.id.tvCatName) TextView tvCatName;
    @BindView(R.id.tvCatContact)TextView tvCatContact;
    @BindView(R.id.tvCatDescription)TextView tvCatDescription;
    @BindView(R.id.bCatAdopt) Button bCatAdopt;
    @BindView(R.id.bLeaveComment) Button bLeaveComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_cat);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(CAT);


        mAuth = FirebaseAuth.getInstance();

        mCat_key = getIntent().getExtras().getString("cat_id");

        mDatabase.child(mCat_key).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String catName = (String) dataSnapshot.child("mCatName").getValue();
                String catContact = (String) dataSnapshot.child("mCatContact").getValue();
                String catPicture = (String) dataSnapshot.child("mCatPicture").getValue();
                String catDescription = (String) dataSnapshot.child("mCatDescription").getValue();
                String mUid = (String) dataSnapshot.child("mUid").getValue();

                tvCatName.setText(catName);
                tvCatContact.setText(catContact);
                tvCatDescription.setText(catDescription);
                Picasso.with(SingleCat.this).load(catPicture).into(ivCatPic);

                if(mAuth.getCurrentUser().getUid().equals(mUid)){

                    bCatAdopt.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bCatAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mCat_key).removeValue();

                Toast.makeText(SingleCat.this, "Thank You!", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(SingleCat.this,MainActivity.class);
                startActivity(mainIntent);

            }
        });

    }

    @OnClick(R.id.bLeaveComment)
    public void leaveComment(){
        Intent comm = new Intent(SingleCat.this,Chat.class);
        startActivity(comm);
    }

}
