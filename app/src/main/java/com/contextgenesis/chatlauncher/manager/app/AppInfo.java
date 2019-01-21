package com.contextgenesis.chatlauncher.manager.app;

import android.content.ComponentName;

public class AppInfo {

    public final ComponentName componentName;
    public final String label;

    public AppInfo(String packageName, String activityName, String label) {
        this.componentName = new ComponentName(packageName, activityName);
        this.label = label;
    }

}
