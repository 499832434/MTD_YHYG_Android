package com.htyhbz.yhyg.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

/**
 * PackageUtils
 * <ul>
 * <strong>Is system application</strong>
 * <li>{@link com.commonlib.utils.PackageUtils#isSystemApplication(android.content.Context)}</li>
 * <li>{@link com.commonlib.utils.PackageUtils#isSystemApplication(android.content.Context, String)}</li>
 * <li>{@link com.commonlib.utils.PackageUtils#isSystemApplication(android.content.pm.PackageManager, String)}</li>
 * </ul>
 * <ul>
 * <strong>Others</strong>
 * <li>{@link com.commonlib.utils.PackageUtils#isTopActivity(android.content.Context, String)} whether the app whost package's name is packageName is on the
 * top of the stack</li>
 * <li>{@link com.commonlib.utils.PackageUtils#startInstalledAppDetails(android.content.Context, String)} start InstalledAppDetails Activity</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-15
 */
public class PackageUtils {

    public static final String TAG = "PackageUtils";

    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            packageName = context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * whether context is system application
     *
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * whether packageName is system application
     *
     * @param packageManager
     * @param packageName
     * @return <ul>
     * <li>if packageManager is null, return false</li>
     * <li>if package name is null or is empty, return false</li>
     * <li>if package name not exit, return false</li>
     * <li>if package name exit, but not system app, return false</li>
     * <li>else return true</li>
     * </ul>
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * whether the app whost package's name is packageName is on the top of the stack
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.GET_TASKS</strong> in manifest</li>
     * </ul>
     *
     * @param context
     * @param packageName
     * @return if params error or task stack is null, return null, otherwise retun whether the app is on the top of
     * stack
     */
    public static boolean isTopActivity(Context context, String packageName) {
        if (context == null || StringUtils.isEmpty(packageName)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (ListUtils.isEmpty(tasksInfo)) {
            return false;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get app version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 返本当前应用的版本名称
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionName;
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * start InstalledAppDetails Activity
     *
     * @param context
     * @param packageName
     */
    public static void startInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == 8 ? "pkg" : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
