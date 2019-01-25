package com.contextgenesis.chatlauncher.ui;

import android.os.Bundle;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.manager.input.InputManager;
import com.contextgenesis.chatlauncher.models.chat.ChatMessage;
import com.contextgenesis.chatlauncher.models.chat.ChatUser;
import com.contextgenesis.chatlauncher.rx.RxBus;
import com.contextgenesis.chatlauncher.rx.scheduler.BaseSchedulerProvider;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerActivity;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends DaggerActivity implements
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener {

    private static final int PERMISSION_REQUEST_CODE = 10;

    @Inject
    RxBus rxBus;
    @Inject
    BaseSchedulerProvider schedulerProvider;
    @Inject
    InputManager inputManager;

    @BindView(R.id.input)
    MessageInput messageInput;
    @BindView(R.id.messagesList)
    MessagesList messagesList;

    private MessagesListAdapter<ChatMessage> messagesAdapter;
    private ChatUser chatUser;
    private ChatUser phone;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        messagesAdapter = new MessagesListAdapter<>("chatUser", null);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);

        messageInput.setInputListener(this);

        disposable.add(registerPermissionsEvent());
    }

    @Override
    protected void onPause() {
        disposable.dispose();
        super.onPause();
    }

    private Disposable registerPermissionsEvent() {
        return rxBus.register(PermissionsEvent.class)
                .subscribeOn(schedulerProvider.androidThread())
                .subscribe(event -> {
                    if (event.getType() == PermissionsEvent.Type.REQUEST) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{event.getPermissions()}, PERMISSION_REQUEST_CODE);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                rxBus.post(new PermissionsEvent(permissions[i], PermissionsEvent.Type.get(grantResults[i])));
            }
        }
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
