package com.crearo.launched;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
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
    private List<ApplicationInfo> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        messagesAdapter = new MessagesListAdapter<>("user", null);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);

        messageInput.setInputListener(this);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.d(TAG, "onLoadMore() called with: page = [" + page + "], totalItemsCount = [" + totalItemsCount + "]");
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Message message = new Message(
                String.valueOf(UUID.randomUUID().getLeastSignificantBits()),
                getUser(),
                input.toString());
        messagesAdapter.addToStart(message, true);
        String packageName = getPackageNameFromName(input.toString());
        if (packageName == null) {
            messagesAdapter.addToStart(new Message("android", getPhone(), "I kinda dumb bro"), false);
        } else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent == null) {
                messagesAdapter.addToStart(new Message("android", getPhone(), "error lol"), false);
            } else {
                messagesAdapter.addToStart(new Message("android", getPhone(), "Launching!"), false);
                startActivity(launchIntent);
            }
        }
        return true;
    }

    @Nullable
    @SuppressLint(value = "DefaultLocale")
    @SuppressWarnings("PMD.UseLocaleWithCaseConversions")
    private String getPackageNameFromName(String msg) {
        String fromClosestPackageName = null;
        String fromClosestName = null;
        for (ApplicationInfo pkg : getAppsList()) {
            if (pkg.name != null) {
                if (pkg.name.equalsIgnoreCase(msg)) {
                    return pkg.name;
                }
                if (pkg.name.toLowerCase().contains(msg.toLowerCase())) {
                    fromClosestName = pkg.packageName;
                }
            }
            if (pkg.packageName != null && pkg.packageName.toLowerCase().contains(msg.toLowerCase())) {
                fromClosestPackageName = pkg.packageName;
            }
        }
        return fromClosestName == null ? fromClosestPackageName : fromClosestName;
    }

    public List<ApplicationInfo> getAppsList() {
        if (packageList == null) {
            packageList = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        }
        return packageList;
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
