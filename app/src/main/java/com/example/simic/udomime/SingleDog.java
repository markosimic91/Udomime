package com.example.simic.udomime;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SingleDog extends AppCompatActivity {

    private static final String DOG = "Dog";
    private String mDog_key = null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @BindView(R.id.ivDogPic) ImageView ivDogPic;
    @BindView(R.id.tvDogName) TextView tvDogName;
    @BindView(R.id.tvDogContact)TextView tvDogContact;
    @BindView(R.id.tvDogDescription)TextView tvDogDescription;
    @BindView(R.id.bDogAdopt) Button bDogAdopt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dog);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(DOG);
        mAuth = FirebaseAuth.getInstance();

        mDog_key = getIntent().getExtras().getString("dog_id");

        mDatabase.child(mDog_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String dogName = (String) dataSnapshot.child("mDogName").getValue();
                String dogContact = (String) dataSnapshot.child("mDogContact").getValue();
                String dogPicture = (String) dataSnapshot.child("mDogPicture").getValue();
                String dogDescription = (String) dataSnapshot.child("mDogDescription").getValue();
                String mUid = (String) dataSnapshot.child("mUid").getValue();

                tvDogName.setText(dogName);
                tvDogContact.setText(dogContact);
                tvDogDescription.setText(dogDescription);
                Picasso.with(SingleDog.this).load(dogPicture).into(ivDogPic);

                if(mAuth.getCurrentUser().getUid().equals(mUid)){

                    bDogAdopt.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bDogAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mDog_key).removeValue();

                Toast.makeText(SingleDog.this, "Thank You!", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(SingleDog.this,MainActivity.class);
                startActivity(mainIntent);

            }
        });


    }
}
