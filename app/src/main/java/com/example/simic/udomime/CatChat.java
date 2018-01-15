package com.example.simic.udomime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatChat extends AppCompatActivity {

    private static final String COMMENTS = "Comments";

    @BindView(R.id.ibSendMessage) ImageButton ibSendMessage;
    @BindView(R.id.MessageInput) EditText MessageInput;
    @BindView(R.id.lvMessages) ListView lvMessage;

    private DatabaseReference mDatabaseCat;
    private String mCatComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_chat);
        ButterKnife.bind(this);

        mCatComment = getIntent().getExtras().getString("cat_comment");

        this.mDatabaseCat = FirebaseDatabase.getInstance().getReference().child(COMMENTS).child(mCatComment);

        ibSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseCat.push().setValue(new Message(MessageInput.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                MessageInput.setText("");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseListAdapter adapterCat = new FirebaseListAdapter<Message>(CatChat.this,
                Message.class,
                R.layout.message_list_item,
                mDatabaseCat) {


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

        lvMessage.setAdapter(adapterCat);
    }
}
