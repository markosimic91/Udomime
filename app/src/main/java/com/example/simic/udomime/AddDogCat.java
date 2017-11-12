package com.example.simic.udomime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDogCat extends AppCompatActivity {

    @BindView(R.id.bAddDog) Button bAddDog;
    @BindView(R.id.bAddCat) Button bAddCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_cat);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.bAddDog)
    public void addDog(){
        Intent addDog = new Intent(AddDogCat.this,AddDog.class);
        startActivity(addDog);
    }

    @OnClick(R.id.bAddCat)
    public void addCat(){
        Intent addCat = new Intent(AddDogCat.this,AddCat.class);
        startActivity(addCat);
    }

}
