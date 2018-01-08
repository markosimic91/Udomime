package com.example.simic.udomime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    private static final String USERS = "Users";
    @BindView(R.id.etUserName) EditText etUserName;
    @BindView(R.id.etUserEmail) EditText etUserEmail;
    @BindView(R.id.etUserPassword) EditText etUserPassword;
    @BindView(R.id.bRegister) Button bRegister;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USERS);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starRegister();
            }
        });
    }

    private void starRegister() {

        final String name = etUserName.getText().toString();
        final String email = etUserEmail.getText().toString();
        String password = etUserPassword.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Signing up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("name").setValue(name);
                        current_user_db.child("image").setValue("default");

                        mProgress.dismiss();
                        
                        Intent intent = new Intent(Register.this,Login.class);
                        startActivity(intent);

                    }else{
                        mProgress.dismiss();
                        Toast.makeText(Register.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }


    }

}
