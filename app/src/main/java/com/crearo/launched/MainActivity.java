package com.crearo.launched;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.input)
    MessageInput messageInput;
    @BindView(R.id.messagesList)
    MessagesList messagesList;
    private MessagesListAdapter<Message> messagesAdapter;
    private User user;
    private User phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        messagesAdapter = new MessagesListAdapter<>("senderId0", null);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);

        messageInput.setInputListener(this);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.d(TAG, "onLoadMore() called with: page = [" + page + "], totalItemsCount = [" + totalItemsCount + "]");
    }

    @Override
    public void onSelectionChanged(int count) {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Message message = new Message(
                String.valueOf(UUID.randomUUID().getLeastSignificantBits()),
                getUser(),
                input.toString());
        messagesAdapter.addToStart(message, true);
        return true;
    }

    private User getUser() {
        if (user == null) {
            user = new User(
                    "user",
                    "Rish",
                    null,
                    true);
        }
        return user;
    }

    private User getPhone() {
        if (phone == null) {
            phone = new User(
                    "phone",
                    "Android",
                    null,
                    true);
        }
        return phone;
    }
}
