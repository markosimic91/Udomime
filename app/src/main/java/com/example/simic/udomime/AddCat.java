package com.example.simic.udomime;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

    GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    private GoogleMap.OnMapClickListener mCustomMapClickListener;

    @BindView(R.id.ibAddCat) ImageButton ibAddCat;
    @BindView(R.id.etCatName) EditText etCatName;
    @BindView(R.id.etCatDesription) EditText etCatDesription;
    @BindView(R.id.etCatContact) EditText etCatContact;
    @BindView(R.id.ivCatImage) ImageView ivCatImage;

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

        bFinish.setOnClickListener(this);
        this.handleEnterEditText();

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
        mProgress.show();


        final String name = etCatName.getText().toString();
        final String desription = etCatDesription.getText().toString();
        final String contact = etCatContact.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desription) && !TextUtils.isEmpty(contact) && mImageUri != null){

            StorageReference filePath = mStorage.child("Cats").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newCat = mDatabaseReference.push();

                    newCat.child("name").setValue(name);
                    newCat.child("desription").setValue(desription);
                    newCat.child("contact").setValue(contact);
                    newCat.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                    startActivity(new Intent(AddCat.this,MainActivity.class));
                }
            });

        }
    }
    //endregion

    //region PutImageToImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            mImageUri = data.getData();

            ivCatImage.setImageURI(mImageUri);

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

    //region EditTextEnter
    private void handleEnterEditText() {
        etCatContact.setInputType(InputType.TYPE_CLASS_TEXT);
        etCatName.setInputType(InputType.TYPE_CLASS_TEXT);
        etCatDesription.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    //endregion
}
