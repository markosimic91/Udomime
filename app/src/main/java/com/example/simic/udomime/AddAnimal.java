package com.example.simic.udomime;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAnimal extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener{

    private static final int PICK_IMAGE = 100;
    private static String ANIMAL = "animal";
    private DatabaseReference mDatabaseReference;

    GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    private GoogleMap.OnMapClickListener mCustomMapClickListener;

    @BindView(R.id.ibAnimalPicure) ImageButton ibAnimalPicure;
    @BindView(R.id.etAnimalName) EditText etAnimalName;
    @BindView(R.id.etAnimalDescription) EditText etAnimalDesription;
    @BindView(R.id.etAnimalContact) EditText etAnimalContact;
    @BindView(R.id.ivAnimalImage) ImageView ivAnimalImage;
    @BindView(R.id.sSelectAnimal) Spinner sSelectAnimal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);
        ButterKnife.bind(this);
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference(ANIMAL);
        this.mDatabaseReference.addValueEventListener(this);
        this.handleEnterEditText();
        this.spinnerSetup();
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

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling  ActivityCompat#requestPermissions
            // See the last example on how to do this.
        }
        this.mGoogleMap.setMyLocationEnabled(true);

    }
    //endregion

    //region Spinner
    private void spinnerSetup() {
        String [] animals = new String []{getString(R.string.select_dog),getString(R.string.select_cat)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,animals);
        sSelectAnimal.setAdapter(adapter);
    }
    //endregion

    //region EditTextEnter
    private void handleEnterEditText() {
        etAnimalContact.setInputType(InputType.TYPE_CLASS_TEXT);
        etAnimalName.setInputType(InputType.TYPE_CLASS_TEXT);
        etAnimalDesription.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    //endregion

    //region GetImage
    @OnClick(R.id.ibAnimalPicure)
    public void openGallery(){
        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery, PICK_IMAGE);
    }
    //endregion

    @OnClick(R.id.bFinish)
    public void animalFinished(){
        String id = this.mDatabaseReference.push().getKey();
        String name = this.etAnimalName.getText().toString();
        String description = this.etAnimalDesription.getText().toString();

    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK){
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivAnimalImage.setImageBitmap(selectedImage);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void uploadImage(Bitmap bitmap){

    }



}
