package com.example.simic.udomime;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Date;

import butterknife.BindView;
import butterknife.ButterKnife;



public class Chat extends AppCompatActivity {

    private FloatingActionButton bSendMessage;
    private EditText etInputMsg;
    private FirebaseListAdapter<Message> mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        displayChatMessages();

        bSendMessage = (FloatingActionButton) findViewById(R.id.bSendMessage);



        bSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etInputMsg = (EditText) findViewById(R.id.etInputMsg);
                FirebaseDatabase.getInstance().getReference().push()
                        .setValue(new Message(etInputMsg.getText().toString(),
                                FirebaseAuth.getInstance().
                                        getCurrentUser().getDisplayName()));

                etInputMsg.setText("");

            }

        });

    }

    private void displayChatMessages() {

        ListView lvMessages = (ListView) findViewById(R.id.lvMessages);

        mAdapter = new FirebaseListAdapter<Message>(
                Chat.this,
                Message.class,
                R.layout.message,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {

                TextView messagesText  = (TextView) v.findViewById(R.id.tvMessageText);
                TextView messagesUser = (TextView) v.findViewById(R.id.tvMessageUser);
                TextView messagesTime = (TextView) v.findViewById(R.id.tvMessageTime);

                messagesText.setText(model.getMsgText());
                messagesUser.setText(model.getMsgUser());
                messagesTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMsgTime()));


            }

        };
        lvMessages.setAdapter(mAdapter);

    }

}
