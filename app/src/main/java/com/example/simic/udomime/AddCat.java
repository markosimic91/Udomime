package com.example.simic.udomime;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.android.gms.tasks.OnFailureListener;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class AddCat extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener {


    private static final int PICK_IMAGE = 100;
    private static String CAT = "cat";

    private DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;

    GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    private GoogleMap.OnMapClickListener mCustomMapClickListener;

    @BindView(R.id.ibAddCat) ImageButton ibAddCat;
    @BindView(R.id.etCatName) EditText etCatName;
    @BindView(R.id.etCatDesription) EditText etCatDesription;
    @BindView(R.id.etCatContact) EditText etCatContact;
    @BindView(R.id.ivCatImage) ImageView ivCatImage;

    @BindView(R.id.bFinish)
    Button bFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);
        ButterKnife.bind(this);
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference(CAT);
        this.mDatabaseReference.addValueEventListener(this);
        this.mStorageReference = FirebaseStorage.getInstance().getReference();

        this.handleEnterEditText();
        this.googleMap();

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

    //region GetImage
    @OnClick(R.id.ibAddCat)
    public void openGallery(){
        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery, PICK_IMAGE);
    }
    //endregion

    @OnClick(R.id.bFinish)
    public void saveAnimal(){

        String id = this.mDatabaseReference.push().getKey();
        String name = this.etCatName.getText().toString();
        String contact = this.etCatContact.getText().toString();
        String desription = this.etCatDesription.getText().toString();
        String  image = this.ivCatImage.setImageURI();

        BitmapDrawable drawable = (BitmapDrawable) ivCatImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Dog dog = new Dog(id,image,name,desription,contact);

        this.mDatabaseReference.child(id).setValue(dog);
        this.etCatName.setText("");
        this.etCatContact.setText("");
        this.etCatDesription.setText("");
        this.ivCatImage.setImageBitmap(bitmap);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        ivCatImage.setImageURI(image);
    }

    private void uploadImage(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(getString(R.string.firebaseURL));
        StorageReference mountainImagesRef = storageReference.child("images/cats");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
