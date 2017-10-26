package com.example.simic.udomime;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.vpPager) ViewPager vpPager;
    @BindView(R.id.tlTabs) TabLayout tlTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.loadFragments();



    }

    private void loadFragments() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.insertFragment(new DogFragment(),DogFragment.TITLE);
        adapter.insertFragment(new CatFragment(),CatFragment.TITLE);
        this.vpPager.setAdapter(adapter);
        this.tlTabs.setupWithViewPager(this.vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addAnimal(MenuItem item) {
        Intent addAnimal = new Intent(MainActivity.this, AddAnimal.class);
        startActivity(addAnimal);

    }
}
