package com.gugr.cases;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gugr.config.ExcelData;
import com.gugr.config.TestConfig;
import com.gugr.model.InterfaceName;
import com.gugr.model.LoginCase;
import com.gugr.utils.ConfigFile;
import com.gugr.utils.DatabaseUtil;

import jxl.read.biff.BiffException;

public class LoginTest {
	
	@BeforeTest(groups = "loginTrue",description = "测试准备工作，获取httpclient对象")
	public void beforeTest() {
		TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
		TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
		TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
		TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSERINFO);
		TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
		
		TestConfig.defaultHttpClient = new DefaultHttpClient();			
	}
	
	@Test(groups = "loginTrue",description = "用户登录成功接口测试")
	public void loginTrue() throws IOException, InterruptedException {
		SqlSession session = DatabaseUtil.getSqlSession();
		
		//返回的对象为LoginCase
		LoginCase loginCase = session.selectOne("loginCase", 1);
		System.out.println(loginCase.toString());
		System.out.println(TestConfig.loginUrl);
		
		//第一步：发送请求
		String result = getResult(loginCase);		
		Thread.sleep(2000);
		//验证结果
		Assert.assertEquals(loginCase.getExpected(), result);
	}

	@Test(groups = "loginfalse",description = "用户登录失败接口测试")
	public void loginFalse() throws IOException {
		SqlSession session = DatabaseUtil.getSqlSession();
		
		LoginCase loginCase = session.selectOne("loginCase", 2);
		System.out.println(loginCase.toString());
		System.out.println(TestConfig.loginUrl);
		//第一步：发送请求
		String result = getResult(loginCase);
		//验证返回结果
		Assert.assertEquals(loginCase.getExpected(),result);
	}
		
	private String getResult(LoginCase loginCase) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(TestConfig.loginUrl);
		JSONObject param = new JSONObject();
		param.put("userName", loginCase.getUsername());
		param.put("password", loginCase.getPassword());
		
		//设置头信息
		post.setHeader("Content-Type", "application/json");
		
		StringEntity entity = new StringEntity(param.toString(),"UTF-8");
		post.setEntity(entity);
		String result;
		HttpResponse response = TestConfig.defaultHttpClient.execute(post);
	    result = EntityUtils.toString(response.getEntity(),"UTF-8");
	    
	    TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
		return result;
	}
}
