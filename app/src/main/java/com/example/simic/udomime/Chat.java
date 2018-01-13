package com.example.simic.udomime;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.BindView;
import butterknife.ButterKnife;


public class Chat extends AppCompatActivity {

    private static final String COMMENTS = "Comments";
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.MessageInput) EditText MessageInput;
    @BindView(R.id.lvMessages) ListView lvMessages;

    private DatabaseReference mDatabasePush;
    private String mDogComment;
    private DatabaseReference mDatabaseDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mDogComment = getIntent().getExtras().getString("dog_comment");

        this.mDatabasePush = FirebaseDatabase.getInstance().getReference().child(COMMENTS).child(mDogComment);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabasePush.push().setValue(new Message(MessageInput.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

                MessageInput.setText("");

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseDisplay = FirebaseDatabase.getInstance().getReference().child(COMMENTS);

        FirebaseListAdapter adapter = new FirebaseListAdapter<Message>(
                Chat.this,
                Message.class,
                R.layout.message_list_item,
                mDatabaseDisplay) {

            @Override
            protected void populateView(View v, Message model, int position) {

                TextView messageUser = (TextView) v.findViewById(R.id.tvMessageUser);
                TextView messageTime = (TextView) v.findViewById(R.id.tvMessageTime);
                TextView messageText = (TextView) v.findViewById(R.id.tvMessageText);

                messageUser.setText(model.getmMessageUser());
                messageText.setText(model.getmMessageText());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getmMessageTime()));

            }
        };
        lvMessages.setAdapter(adapter);

    }

}