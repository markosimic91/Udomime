package com.example.simic.udomime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDog extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener,View.OnClickListener {


    private static final int PICK_IMAGE = 100;
    private static String DOG = "Dog";
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Uri mImageUri = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUsers;
    private GoogleMap.OnMapClickListener mCustomMapClickListener;


    GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    GeoFire mGeoFire;


    //region BindView
    @BindView(R.id.ibAddDog) ImageButton ibAddDog;
    @BindView(R.id.etDogName) EditText etDogName;
    @BindView(R.id.etDogDescription) EditText etDogDescription;
    @BindView(R.id.etDogContact) EditText etDogContact;
    @BindView(R.id.bFinish) Button bFinish;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);
        ButterKnife.bind(this);
        mProgress = new ProgressDialog(this);
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference(DOG);
        this.mDatabaseReference.addValueEventListener(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUsers = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUsers.getUid());
        mGeoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Dog Location"));

        bFinish.setOnClickListener(this);
        this.googleMap();

        //region OnClickLiseners
        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishDog();
            }
        });

        ibAddDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,PICK_IMAGE);
            }
        });
        //endregion

    }
    //region UploadDogMethod
    private void publishDog() {
        mProgress.setMessage("Uploading...");
        mProgress.show();


        final String name = etDogName.getText().toString();
        final String description = etDogDescription.getText().toString();
        final String contact = etDogContact.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(contact) && mImageUri != null){

            mProgress.show();

            StorageReference filePath = mStorage.child("Dogs").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newDog = mDatabaseReference.push();
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newDog.child("mDogName").setValue(name);
                            newDog.child("mDogDescription").setValue(description);
                            newDog.child("mDogContact").setValue(contact);
                            newDog.child("mDogPicture").setValue(downloadUrl.toString());
                            newDog.child("mUid").setValue(mCurrentUsers.getUid());
                            newDog.child("mUserName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        startActivity(new Intent(AddDog.this,MainActivity.class));
                                        Toast.makeText(AddDog.this, "Your dog is ready for adoption!", Toast.LENGTH_SHORT).show();
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

        }else{
            mProgress.dismiss();
            Toast.makeText(this, "Please check your input!", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
    

    //region GoogleMap
    private void googleMap() {
        this.mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.GoogleMap);
        this.mMapFragment.getMapAsync(this);
        this.mCustomMapClickListener = new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions marker = new MarkerOptions();
                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_black_24dp)) ;
                marker.position(latLng);
                marker.draggable(true);
                mGoogleMap.addMarker(marker);

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


    //region GetImage
    @OnClick(R.id.ibAddDog)
    public void openGallery(){
        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery, PICK_IMAGE);
    }
    //endregion

    //region PutImageToImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            ibAddDog.setImageURI(mImageUri);

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


}
