package com.microtest.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class RestClient {

	public ExtentReports extent=new ExtentReports("C:\\Users\\rpand\\eclipse-workspace\\microservicetester-poc\\microservicetester-poc\\microservicetester-poc\\test-output\\APITESTRESULT.html",false);
	public ExtentTest test;
	
	
	//1. GET Method for Failure
		
	public void executeJson(String method) {
		
		switch(method) {
		  case "GET":
		    // code block
			  System.out.println("GET Method called");
			  test=extent.startTest("FAILURE Scenarios: GET API Called"); //for reporting
			  test.log(LogStatus.INFO, "Test Case No: "+(1));
			  test.log(LogStatus.FAIL, method);
			  test.log(LogStatus.INFO, "Wrong URL");
		    break;
		  case "POST":
			  System.out.println("POST Method called");
		    // code block
		    break;
		  case "UPDATE":
			  System.out.println("POST Method called");
		    // code block
		    break;
		  default:
		    // code block
		}
		extent.endTest(test);
		extent.flush();
	}
	
	public void get(String url) {
		
		try {
		
		test=extent.startTest("GET API Called"); //for reporting
		test.log(LogStatus.INFO, url);
		
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpGet httpGet=new HttpGet(url); //http get request
		CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);  //hit the GET Request
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code-----:"+statusCode);
		test.log(LogStatus.INFO, "Status Code: "+statusCode);
		
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		System.out.println("GET responseString:\n"+responseString);
		test.log(LogStatus.INFO, "Response from Server: "+responseString);
		
//		JSONObject responseJson=new JSONObject(responseString);
//		System.out.println("Response JSON from API--> "+responseJson);
		//Get Headers
		Header[] headersArray=closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allHeaders=new HashMap<String, String>();
		
		for(Header header: headersArray) {
			allHeaders.put(header.getName(), header.getValue());
		}
		System.out.println("Headers Array--> \n"+allHeaders);
		test.log(LogStatus.INFO, "All Headers: "+allHeaders);
		
		extent.endTest(test);
		extent.flush();
	}
		
		catch (Exception ex) {
			System.out.println(ex);
			}
	    		
			
	}
	
	//POST Method
	
	public void post(String url, ArrayList<JSONObject> body) throws ClientProtocolException, IOException {
		
		JSONObject jsonBody = new JSONObject();
		//Here we have to handle the headers and Body
//		
//		jsonBody.put("id", 11);
//		jsonBody.put("fname", "Test");
//		jsonBody.put("lname", "Test");
//		jsonBody.put("age", "23");
//		jsonBody.put("sex", "male");
		
		test=extent.startTest("POST API Called: ");
		
		
		
		for(int i=0; i<body.size();i++) {
	
		test.log(LogStatus.INFO, "Test Case No: "+(i+1)); //for reporting
		test.log(LogStatus.INFO, "URL: "+url);
			
		jsonBody=body.get(i);
		System.out.println("Jason Body: "+jsonBody);
		test.log(LogStatus.INFO, "Request Body: "+jsonBody);
	
		
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
		    HttpPost request = new HttpPost(url);
		    HttpResponse response;		    
		    StringEntity params = new StringEntity(jsonBody.toString());
		    request.addHeader("content-type", "application/json");
		    test.log(LogStatus.INFO, "Header : "+request);
		    request.setEntity(params);
		    response=httpClient.execute(request);
		    
		    System.out.println("Response got from Server: \n"+response.toString());
		    // handle response here...
		    
		    int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("Status Code-----:"+statusCode);
			
			if(statusCode!=201) {
			test.log(LogStatus.FAIL, "Failure Status Code : "+statusCode);
			String responseString = response.getStatusLine().toString();
			System.out.println("responseString JSON from API--> \n"+responseString);
			test.log(LogStatus.INFO, "Response from Server : "+responseString);
			}
			else
				test.log(LogStatus.PASS, "Status Code : "+statusCode);
			
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println("responseString JSON from API--> \n"+responseString);
			test.log(LogStatus.INFO, "Response from Server : "+responseString);
//			
//			JSONObject responseJson=new JSONObject();
//			System.out.println("Response JSON from API--> "+responseJson);
			
			Header[] headersArray=response.getAllHeaders();
			HashMap<String, String> allHeaders=new HashMap<String, String>();
			
			for(Header header: headersArray) {
				allHeaders.put(header.getName(), header.getValue());
			}
			System.out.println("Headers Array--> \n"+allHeaders);
			test.log(LogStatus.INFO, "Headers from Server : "+allHeaders);
		    
			extent.endTest(test);
			extent.flush();
		    
		} catch (Exception ex) {
		    // handle exception here
		} finally {
		    httpClient.close();
		}
		
		//Handle the reporting here
		
		}
		

	}
	
}	



