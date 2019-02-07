package com.contextgenesis.chatlauncher.ui;

import android.os.Bundle;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.events.InputMessageEvent;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.fluidresize.FluidContentResizer;
import com.contextgenesis.chatlauncher.manager.input.InputManager;
import com.contextgenesis.chatlauncher.manager.suggest.SuggestionManager;
import com.contextgenesis.chatlauncher.manager.suggest.SuggestionTextWatcher;
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
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends DaggerAppCompatActivity implements
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener, MessageInput.AttachmentsListener {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private final CompositeDisposable disposable = new CompositeDisposable();
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
    @BindView(R.id.shortcuts)
    ShortcutsCardView shortcuts;

    private MessagesListAdapter<ChatMessage> messagesAdapter;
    private ChatUser chatUser;
    private ChatUser phone;

    @Inject
    SuggestionManager suggestionManager;
    @Inject
    SuggestionTextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        new FluidContentResizer(this);

        messagesAdapter = new MessagesListAdapter<>("chatUser", null);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);
        messageInput.setAttachmentsListener(this);

        messageInput.setInputListener(this);
        messageInput.getInputEditText().addTextChangedListener(textWatcher);

        Observable
                .create(emitter -> emitter.onNext(new Object()))
                .observeOn(schedulerProvider.runOnBackground())
                .subscribeOn(schedulerProvider.runOnBackground())
                .subscribe(__ -> suggestionManager.initialize());
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable.add(registerPermissionsEvent());
        disposable.add(registerOutputMessageEvent());
        disposable.add(registerInputMessageEvent());
    }

    @Override
    protected void onStop() {
        disposable.clear();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (shortcuts.isVisible()) {
            shortcuts.hide(getShortcutButtonX());
        } else {
            super.onBackPressed();
        }
    }

    private Disposable registerPermissionsEvent() {
        return rxBus.register(PermissionsEvent.class)
                .observeOn(schedulerProvider.androidThread())
                .subscribeOn(schedulerProvider.androidThread())
                .subscribe(event -> {
                    if (event.getType() == PermissionsEvent.Type.REQUEST) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{event.getPermissions()}, PERMISSION_REQUEST_CODE);
                    }
                });
    }

    private Disposable registerOutputMessageEvent() {
        return rxBus.register(OutputMessageEvent.class)
                .observeOn(schedulerProvider.androidThread())
                .subscribeOn(schedulerProvider.androidThread())
                .subscribe(event -> {
                    messagesAdapter.addToStart(new ChatMessage("android", getPhone(),
                            event.getMessage()), true);
                });
    }

    private Disposable registerInputMessageEvent() {
        return rxBus.register(InputMessageEvent.class)
                .observeOn(schedulerProvider.androidThread())
                .subscribeOn(schedulerProvider.androidThread())
                .subscribe(event -> {
                    if (event.isNeedsMoreInput()) {
                        messageInput.getInputEditText().setText(event.getMessage());
                        messageInput.getInputEditText().requestFocus();
                        messageInput.getInputEditText().setSelection(messageInput.getInputEditText().length());
                    } else {
                        messageInput.getInputEditText().setText(event.getMessage());
                        messageInput.getButton().performClick();
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

        // run in background
        Observable
                .create(emitter -> emitter.onNext(new Object()))
                .observeOn(schedulerProvider.runOnBackground())
                .subscribeOn(schedulerProvider.runOnBackground())
                .subscribe(__ -> inputManager.executeInput(userInputMessage.getText()));
        return true;
    }

    @Override
    public void onAddAttachments() {
        if (shortcuts.isVisible()) {
            shortcuts.hide(getShortcutButtonX());
        } else {
            shortcuts.show(getShortcutButtonX());
        }
    }

    /**
     * Gets the reveal position for the attachment button based on screen width and view width
     */
    private int getShortcutButtonX() {
        return (int) messageInput.getAttachmentButton().getX();
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
