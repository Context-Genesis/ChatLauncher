package com.contextgenesis.chatlauncher.manager.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class AppManager {

    private final PackageManager packageManager;
    private List<AppInfo> appList;

    public AppManager(PackageManager packageManager) {
        this.packageManager = packageManager;
        appList = getAppList();
    }

    public AppInfo getAppInfoFromName(String name) {
        for (AppInfo appInfo : appList) {
            if (appInfo.getLabel().equalsIgnoreCase(name)) {
                return appInfo;
            }
        }
        return null;
    }

    private List<AppInfo> getAppList() {
        if (appList == null) {
            appList = new ArrayList<>();
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(i, 0);
            for (ResolveInfo resolveInfo : resolveInfoList) {
                appList.add(new AppInfo(resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name,
                        resolveInfo.loadLabel(packageManager).toString()));
            }
        }

        return appList;
    }

    public boolean launchApp(Context context, @Nullable AppInfo appInfo) {
        if (appInfo == null) {
            return false;
        }
        Intent launchIntent = packageManager.getLaunchIntentForPackage(appInfo.componentName.getPackageName());
        if (launchIntent == null) {
            return false;
        }
        try {
            context.startActivity(launchIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

}
