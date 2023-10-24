package com.pac.imonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ContactDetailsActivity extends AppCompatActivity implements MessageAdapter.MessageAdapterEventListener {
    private static final String KEY_CONTACT_ID = "contactId";

    public static void startActivity(Context context, long contactId) {
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtra(ContactDetailsActivity.KEY_CONTACT_ID, contactId);
        context.startActivity(intent);
    }

    private Contact contact;
    private MessageAdapter messageAdapter;
    private EditText editTextMessage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        this.editTextMessage = findViewById(R.id.editTextMessage);
        recyclerView = findViewById(R.id.recyclerViewMessage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            long contactId = bundle.getLong(KEY_CONTACT_ID, -1);

            AppDatabase db = AppDatabase.getInstance(this);
            ContactDao contactDao = db.getContactDao();
            this.contact = contactDao.getById(contactId);
            this.messageAdapter = new MessageAdapter(this);
            recyclerView.setAdapter(this.messageAdapter);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        this.refreshMessageList();
    }

    @Override
    public void onMessageClicked(long messageId) {
        // Implement your logic when a message is clicked (if needed).
    }

    @Override
    public void onMessageLongClicked(long messageId) {
        // Implement your logic when a message is long-clicked (if needed).
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/ HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public void sendMessage(View view) {
        String messagetext = this.editTextMessage.getText().toString();
        if (messagetext.isEmpty()) {
            CharSequence text = "Mensagem em branco";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }

        editTextMessage.getText().clear();
        String time = getCurrentTime();
        Message newMessage = new Message(0, messagetext, "", messagetext, "", "", false, this.contact.getId());
        newMessage.setTimestamp(getCurrentTime());
        AppDatabase.getInstance(this).getMessageDao().insert(newMessage);

        if (messagetext.toLowerCase().equals("ola")) {
            contactReply();
        }
        refreshMessageList();
    }

    private void contactReply() {
        String contactReply1 = "Hi there!";
        String contactReply2 = "Hi there! 2";
        String contactReply3 = "Hi there! 3";
        String[] replies = {contactReply1, contactReply2, contactReply3};
        Random replypicker = new Random();
        int index = replypicker.nextInt(replies.length);
        String randomReply = replies[index];
        Message newMessage = new Message(0, "", "", "", randomReply, " ", true, this.contact.getId());
        newMessage.setTimestamp(getCurrentTime());
        AppDatabase.getInstance(this).getMessageDao().insert(newMessage);
    }

    private void refreshMessageList() {
        List<Message> newMessageList = AppDatabase.getInstance(this).getMessageDao().getMessagesByChat(this.contact.getId());
        this.messageAdapter.refreshList(newMessageList);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(newMessageList.size() - 1);
            }
        });
    }
}
