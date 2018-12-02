package com.gugr.autotest;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gugr.config.ExcelData;

import jxl.read.biff.BiffException;

public class CalculatorExcel {

//    Calculator cal=new Calculator();

    @DataProvider(name="num")
    public Object[][] Numbers() throws BiffException, IOException{
        ExcelData e=new ExcelData("testdata", "calculator");
        return e.getExcelData();
    }
    @Test(dataProvider="num")
    public void testAdd(HashMap<String, String> data){
//        System.out.println(data.toString());
//        String userName=data.get("userName");
//        String password=data.get("password");
    	  String login_status=data.get("login_status");  
    	  Assert.assertEquals(login_status, "true" );
//        System.out.printf(userName,password,login_status);
    	  System.out.println("有效数据行数为：" + data.size());
    }
}
