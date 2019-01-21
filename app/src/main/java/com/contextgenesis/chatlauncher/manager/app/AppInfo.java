package com.contextgenesis.chatlauncher.manager.app;

import android.content.ComponentName;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppInfo {

    public final ComponentName componentName;
    public final String label;

    public AppInfo(String packageName, String activityName, String label) {
        this.componentName = new ComponentName(packageName, activityName);
        this.label = label;
    }

}
