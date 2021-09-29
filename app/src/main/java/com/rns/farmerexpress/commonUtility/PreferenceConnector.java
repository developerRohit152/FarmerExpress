package com.rns.farmerexpress.commonUtility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class PreferenceConnector {
	public static final String 	PREF_NAME 						= "app_prefrences";
	public static final int 	MODE 							= Context.MODE_PRIVATE;

	public static final String 	loginstatus 					= "loginstatus";
	public static final String 	profilestatus 					= "profilestatus";
	public static final String 	profileName 					= "profilename";
	public static final String 	profilePic 					= "profilepic";
	public static final String 	money 					= "money";
	public static final String 	stateName 					= "state";
	public static final String 	opName 					= "opnamw";
	public static final String 	USER_PHONE 					= "userPhone";
	public static final String 	MOBILE 					= "mobile";
	public static final String 	SHARE 					= "True";




	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static String writeStringone(Context context, String key, String string) {
		getEditor(context).putString(key, string).commit();
		return string;
	}
	public static void writeString(Context context, String key, String string) {
		getEditor(context).putString(key, string).commit();
	}
	public static void writeString1(Context context, String key, String damLogModel) {
		getEditor(context).putString(key, damLogModel).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void cleanPrefrences(Context context){
		getPreferences(context).edit().clear().commit();
	}

	public static Map<String, ?> getAllKeys(Context context) {
		Map<String, ?> map = getPreferences(context).getAll();
		return map;
	}
}
