package com.htyhbz.yhyg.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

	
	public static final void putString(Context context, String prefKey, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static final void putBoolean(Context context, String prefKey, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static final void putFloat(Context context, String prefKey, String key, float value) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().putFloat(key, value).commit();
	}
	
	public static final void putInt(Context context, String prefKey, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	
	public static final void putLong(Context context, String prefKey, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}

	public static final String getString(Context context, String prefKey, String key, String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}
	
	public static final boolean getBoolean(Context context, String prefKey, String key, boolean defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}
	
	public static final float getFloat(Context context, String prefKey, String key, float defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sp.getFloat(key, defaultValue);
	}
	
	public static final int getInt(Context context, String prefKey, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}
	
	public static final long getLong(Context context, String prefKey, String key, long defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sp.getLong(key, defaultValue);
	}
	
	public static final void clearKey(Context context, String prefKey) {
		SharedPreferences sp = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
}
