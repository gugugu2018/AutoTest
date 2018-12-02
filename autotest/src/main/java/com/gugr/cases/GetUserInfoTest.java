package com.gugr.cases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.gugr.config.TestConfig;
import com.gugr.model.GetUserInfoCase;
import com.gugr.model.User;
import com.gugr.utils.DatabaseUtil;

public class GetUserInfoTest {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(dependsOnGroups = "loginTrue",description = "获取userId为1的用户信息")
	public void getUserInfo() throws IOException, InterruptedException {
		SqlSession session = DatabaseUtil.getSqlSession();
		GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase", 1);
		System.out.println(getUserInfoCase.toString());
		System.out.println(TestConfig.getUserInfoUrl);
		
		//发送请求(调用接口)返回结果
		JSONArray resultJson = getJsonResult(getUserInfoCase);
		
        Thread.sleep(2000);
        User user = session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        System.out.println("自己查库获取用户信息:"+user.toString());

        List userList = new ArrayList();
        userList.add(user);
        JSONArray jsonArray = new JSONArray(userList);
        JSONArray jsonArray1 = new JSONArray(resultJson.getString(0));
        System.out.println("获取用户信息:"+jsonArray.toString());
        System.out.println("调用接口获取用户信息:"+resultJson.toString());
        Assert.assertEquals(jsonArray.toString(),jsonArray1.toString());
    }

	@SuppressWarnings("rawtypes")
	private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
		JSONObject param = new JSONObject();
		param.put("id", getUserInfoCase.getUserId());
		post.setHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(param.toString(),"UTF-8");
		post.setEntity(entity);
		
		TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);
		
		HttpResponse response = TestConfig.defaultHttpClient.execute(post);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		
		List resultList = Arrays.asList(result);
		JSONArray array = new JSONArray(resultList);
		
		return array;
	}

}
