package com.gugr.cases;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.gugr.config.TestConfig;
import com.gugr.model.AddUserCase;
import com.gugr.model.User;
import com.gugr.utils.DatabaseUtil;

public class AddUserTest {
	
	@Test(dependsOnGroups = "loginTrue",description = "添加用户接口测试")
	public void addUser() throws IOException, InterruptedException {		
		SqlSession session = DatabaseUtil.getSqlSession();
		AddUserCase addUserCase = session.selectOne("addUserCase",1);
		System.out.println(addUserCase.toString());
		System.out.println(TestConfig.addUserUrl);
		
		//发请求，获取结果
		String result = getResult(addUserCase);
		
        //查询用户看是否添加成功
        Thread.sleep(3000);
		//验证返回结果
		User user = session.selectOne("addUser", addUserCase);
		System.out.println(user.toString());
		
		Assert.assertEquals(addUserCase.getExpected(), result);
	}

	private String getResult(AddUserCase addUserCase) throws IOException {
		HttpPost post = new HttpPost(TestConfig.addUserUrl);
		JSONObject param = new JSONObject();
		param.put("userName", addUserCase.getUserName());
		param.put("password", addUserCase.getPassword());
		param.put("sex", addUserCase.getSex());
		param.put("age", addUserCase.getAge());
		param.put("permission", addUserCase.getPermission());
		param.put("isDelete", addUserCase.getIsDelete());
		
		//设置头信息
		post.setHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(param.toString(),"UTF-8");
		post.setEntity(entity);
		
		//设置cookies
		TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);
		String result;	//存放返回结果
		
		//执行请求
		HttpResponse response = TestConfig.defaultHttpClient.execute(post);
		result = EntityUtils.toString(response.getEntity(), "UTF-8");		
		
		System.out.println(result);
		return result;
	}

}
