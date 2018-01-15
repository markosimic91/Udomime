package com.example.simic.udomime;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String USERS = "Users";
    @BindView(R.id.vpPager) ViewPager vpPager;
    @BindView(R.id.tlTabs) TabLayout tlTabs;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.loadFragments();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this,Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(USERS);
        mDatabaseUsers.keepSynced(true);

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

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    //region ActionBarIntents
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_register:
                Intent register = new Intent(MainActivity.this,Register.class);
                startActivity(register);
                return true;
            case R.id.menu_login:
                Intent login = new Intent(MainActivity.this,Login.class);
                startActivity(login);
                return true;
            case R.id.menu_addCat:
                if(mAuth != null) {

                    Intent addCat = new Intent(MainActivity.this, AddCat.class);
                    startActivity(addCat);

                }else{

                    Toast.makeText(this, "Please login in!", Toast.LENGTH_SHORT).show();

                }
                return true;
            case R.id.menu_addDog:
                if (mAuth != null){

                Intent addDog = new Intent(MainActivity.this,AddDog.class);
                startActivity(addDog);

                }else {

                    Toast.makeText(this, "Please login in!", Toast.LENGTH_SHORT).show();

                }
                return true;
            case R.id.menu_logout:
                logout();

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void logout() {
        mAuth.signOut();
    }
    //endregion

}
