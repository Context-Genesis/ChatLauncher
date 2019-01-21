package com.contextgenesis.chatlauncher.ui;

import android.os.Bundle;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.manager.input.InputManager;
import com.contextgenesis.chatlauncher.models.chat.ChatMessage;
import com.contextgenesis.chatlauncher.models.chat.ChatUser;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener {

    @BindView(R.id.input)
    MessageInput messageInput;
    @BindView(R.id.messagesList)
    MessagesList messagesList;

    private MessagesListAdapter<ChatMessage> messagesAdapter;
    private ChatUser chatUser;
    private ChatUser phone;
    private InputManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        messagesAdapter = new MessagesListAdapter<>("chatUser", null);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);

        messageInput.setInputListener(this);

        inputManager = new InputManager();
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Timber.d("onLoadMore() called with: page = [" + page + "], totalItemsCount = [" + totalItemsCount + "]");
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        ChatMessage userInputMessage = new ChatMessage(
                String.valueOf(UUID.randomUUID().getLeastSignificantBits()),
                getChatUser(),
                StringUtils.trim(input.toString()));
        messagesAdapter.addToStart(userInputMessage, true);

        String response = inputManager.executeInput(userInputMessage.getText());
        messagesAdapter.addToStart(new ChatMessage("android", getPhone(), response), false);
        return true;
    }

    private ChatUser getChatUser() {
        if (chatUser == null) {
            chatUser = new ChatUser(
                    "chatUser",
                    "Rish",
                    null,
                    true);
        }
        return chatUser;
    }

    private ChatUser getPhone() {
        if (phone == null) {
            phone = new ChatUser(
                    "phone",
                    "Android",
                    null,
                    true);
        }
        return phone;
    }
}
