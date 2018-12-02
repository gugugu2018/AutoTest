package com.gugr.cases;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.gugr.config.TestConfig;
import com.gugr.model.UpdateUserInfoCase;
import com.gugr.model.User;
import com.gugr.utils.DatabaseUtil;

public class UpdateUserInfoTest {
	
	@Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
	public void updateUserInfo() throws IOException, InterruptedException {
		SqlSession session = DatabaseUtil.getSqlSession();
		UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", 1);
		System.out.println(updateUserInfoCase.toString());
		System.out.println(TestConfig.updateUserInfoUrl);
		//发送请求返回结果
		int result = getRestul(updateUserInfoCase);
		//验证结果
		Thread.sleep(3000);
		User user = session.selectOne(updateUserInfoCase.getExpected(), updateUserInfoCase);
		
		Assert.assertNotNull(user);
		Assert.assertNotNull(result);
	}
	
	@Test(dependsOnGroups = "loginTrue",description = "删除用户信息")
	public void deleteUser() throws IOException {
		SqlSession session = DatabaseUtil.getSqlSession();
		UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", 2);
		System.out.println(updateUserInfoCase.toString());
		System.out.println(TestConfig.updateUserInfoUrl);
		
		//发送请求返回结果
		int result = getRestul(updateUserInfoCase);
		//验证结果
		User user = session.selectOne(updateUserInfoCase.getExpected(), updateUserInfoCase);
		
		Assert.assertNotNull(user);
		Assert.assertNotNull(result);
	}
	
	private int getRestul(UpdateUserInfoCase updateUserInfoCase) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
		JSONObject param = new JSONObject();
		param.put("id", updateUserInfoCase.getUserId());
		param.put("userName", updateUserInfoCase.getUserName());
		param.put("sex", updateUserInfoCase.getSex());
		param.put("age", updateUserInfoCase.getAge());
		param.put("permission", updateUserInfoCase.getPermission());
		param.put("isDelete", updateUserInfoCase.getIsDelete());
		
		post.setHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(param.toString(),"UTF-8");
		post.setEntity(entity);
		
		TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);
		
		HttpResponse response = TestConfig.defaultHttpClient.execute(post);		
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		
		return Integer.parseInt(result);
	}
}
