package com.hc.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedpfTools {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private static SharedpfTools instance;

	public SharedpfTools(Context context) {
		preferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		editor = preferences.edit();
		editor.apply();
	}

	public static SharedpfTools getInstance(Context context) {
		if (instance == null) {
			instance = new SharedpfTools(context);
		}
		return instance;
	}

	/**
	 * 用户id、
	 * 
	 * @param id
	 */
	public void setUserID(String id) {
		editor.putString("id", id);
		editor.commit();
	}

	public String getUserID() {
		return preferences.getString("id", "0");
	}
	
	public String getLoginMethod() {
		return preferences.getString("loginmethod", "0");
	}
	
	public void setLoginMethod(String loginmethod) {
		editor.putString("loginmethod", loginmethod);
		editor.commit();
	}

	public void setAccessToken(String accessToken) {
		editor.putString("accessToken", accessToken);
		editor.commit();
	}

	public String getAccessToken() {
		return preferences.getString("accessToken", "bf0842736063281868a1827659aa356841ae97a3");
	}

	public void setAppsercert(String appsercert) {
		editor.putString("appsercert", appsercert);
		editor.commit();
	}

	public String getAppsercert() {
		return preferences.getString("appsercert", "ODg4ODg4");
	}

	public void setPosition(String position) {
		editor.putString("position", position);
		Log.v("position", position);
		editor.commit();
	}

	public String getPosition() {
		return preferences.getString("position", "0");
	}

	public void setDepartmentId(String departmentId) {
		editor.putString("departmentId", departmentId);
		editor.commit();
	}

	public String getDepartmentId() {
		return preferences.getString("departmentId", "0");
	}

	public void setLogStatus(boolean logStatus) {
		editor.putBoolean("logStatus", logStatus);
		editor.commit();
	}

	public boolean getLogStatus() {
		return preferences.getBoolean("logStatus", false);
	}

	public void setNickName(String nickname) {
		editor.putString("nickname", nickname);
		editor.commit();
	}

	public String getNickName() {
		return preferences.getString("nickname", "0");
	}

	public void setUserType(int usertype) {
		editor.putInt("usertype", usertype);
		editor.commit();
	}

	public int getUserType() {
		return preferences.getInt("usertype", 0);
	}

	public void setSearchHistory(String searchHistory) {
		editor.putString("searchHistory", searchHistory);
		editor.commit();
	}

	public String getSearchHistory() {
		return preferences.getString("searchHistory", "");
	}

	public void clear() {
		editor.remove("id");
		editor.remove("accessToken");
		editor.remove("appsercert");
		editor.remove("position");
		editor.remove("departmentId");
		editor.remove("nickname");
		editor.remove("usertype");
		editor.remove("searchHistory");
		editor.commit();
	}
}
