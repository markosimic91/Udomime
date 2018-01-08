package com.example.simic.udomime;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class AddCat extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener,View.OnClickListener {

    private static final int PICK_IMAGE = 100;
    private static String CAT = "Cat";
    private Uri mImageUri = null;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    private GoogleMap.OnMapClickListener mCustomMapClickListener;

    @BindView(R.id.ibAddCat) ImageButton ibAddCat;
    @BindView(R.id.etCatName) EditText etCatName;
    @BindView(R.id.etCatDescription) EditText etCatDescription;
    @BindView(R.id.etCatContact) EditText etCatContact;
    @BindView(R.id.bFinish) Button bFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);
        ButterKnife.bind(this);
        mProgress = new ProgressDialog(this);
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CAT);
        this.mDatabaseReference.addValueEventListener(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        bFinish.setOnClickListener(this);

        this.googleMap();

        //region OnClickLiseners
        ibAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,PICK_IMAGE);

            }
        });

        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                publishCat();

            }
        });

        //endregion

    }


    //region UploadMethod
    private void publishCat() {

        mProgress.setMessage("Uploading...");



        final String name = etCatName.getText().toString();
        final String desription = etCatDescription.getText().toString();
        final String contact = etCatContact.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desription) && !TextUtils.isEmpty(contact) && mImageUri != null){

            mProgress.show();

            StorageReference filePath = mStorage.child("Cats").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newCat = mDatabaseReference.push();

                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    newCat.child("mCatName").setValue(name);
                                    newCat.child("mCatDescription").setValue(desription);
                                    newCat.child("mCatContact").setValue(contact);
                                    newCat.child("mCatPicture").setValue(downloadUrl.toString());
                                    newCat.child("mUid").setValue(mCurrentUser.getUid());
                                    newCat.child("mUserName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(AddCat.this,MainActivity.class));
                                                Toast.makeText(AddCat.this, "Your cat is ready for adoption!", Toast.LENGTH_SHORT).show();
                                            }
                                            
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {



                                }
                            });

                        mProgress.dismiss();

                        }

                    });


        }else {
            mProgress.dismiss();
            Toast.makeText(this, "Please check your input!", Toast.LENGTH_SHORT).show();
        }
    }



    //endregion


    //region PutImageToImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            mImageUri = data.getData();

            ibAddCat.setImageURI(mImageUri);

        }
    }
    //endregion

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    public void onClick(View v) {

    }

    //region GoogleMap
    private void googleMap() {
        this.mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.GoogleMap);
        this.mMapFragment.getMapAsync(this);
        this.mCustomMapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                markerOptions.title("Im here");
                markerOptions.position(latLng);
                mGoogleMap.addMarker(markerOptions);
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        UiSettings uiSettings = this.mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        this.mGoogleMap.setOnMapClickListener(this.mCustomMapClickListener);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling  ActivityCompat#requestPermissions
            // See the last example on how to do this.
        }
        this.mGoogleMap.setMyLocationEnabled(true);

    }
    //endregion

}
