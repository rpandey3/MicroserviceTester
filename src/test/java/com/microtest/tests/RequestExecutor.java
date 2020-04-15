package com.microtest.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.microtest.base.TestBase;
import com.microtest.client.RestClient;
import com.microtest.jsonmanipulator.JsonManipulator;

@Listeners(com.microtest.tests.ExtentReport.class)
public class RequestExecutor extends TestBase{

	public RequestExecutor() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	TestBase testBest;
	JsonManipulator objJsonManipulator=new JsonManipulator();
	String serviceurl;
	String resourcePath;
	String url;
	RestClient objRestClient;
	
	
	
	@BeforeMethod
	public void setUp() throws IOException {
		testBest=new TestBase();
		
		//body=new JSONObject(payload.jsonList);
		//payload=objJsonManipulator.writeToJSONFile();
		serviceurl=prop.getProperty("URL");
		resourcePath=prop.getProperty("resource");
		
		url=serviceurl+resourcePath;
				
	}
	
	@Test
	public void getAPITest() throws ClientProtocolException, IOException {
		objRestClient=new RestClient();
		objRestClient.get(url);		
	}
	
	@Test
	public void postAPITest() throws IOException {
		objRestClient=new RestClient();
		
		//objJsonManipulator.
		objRestClient.post(url, objJsonManipulator.jsonList);
		
	}
	
	@Test
	public void APITest() {
		String method="GET";
		objRestClient=new RestClient();
		objRestClient.executeJson(method);;	
				
	}
}
