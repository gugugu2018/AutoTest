package com.gugr.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestConfig {
	
	//对应application中的接口
	public static String loginUrl;		//只加载一次用static
	public static String updateUserInfoUrl;
	public static String getUserListUrl;
	public static String getUserInfoUrl;
	public static String addUserUrl;
	
	public static DefaultHttpClient defaultHttpClient;
	public static CookieStore store;
}
