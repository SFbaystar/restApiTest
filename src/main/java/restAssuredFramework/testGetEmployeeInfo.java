package restAssuredFramework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*
 * Rest API test with a public site - www.restapiexample.com
 * 
 * Java Version 1.8; TestNG Version 6.14.2
 */

// ====== Test Data ======
// {"status":"success",
// "data": [
// {"id":1,"employee_name":"Tiger Nixon","employee_salary":320800,"employee_age":61,"profile_image":""},
// ...
// {"id":11,"employee_name":"Jena Gaines","employee_salary":90560,"employee_age":30,"profile_image":""},
// ...
//
// ],
// "message":"Successfully! All records has been fetched."}
//

public class testGetEmployeeInfo {
	
	final static String Public_URL = "https://dummy.restapiexample.com";

	/*
	 *  Get All Employee data and verify each employee's record
	 */
	@Test
	public void verifyEmployees() {
		
		RestAssured.baseURI = Public_URL + "/api/v1/employees";
		
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON);
		
		//Get Employee data from public site
		Response response = httpRequest.get();
		
		// Start parsing
	    JSONObject obj = new JSONObject(response.asString());
	    //System.out.println(obj.toString());
	    
	    // get and verify response status
	    String status = obj.getString("status");
	    Assert.assertEquals(status, "success");
	    
	    // get Array type
	    JSONArray results = obj.getJSONArray("data");
	    
	    // SPL-001: No two movies should have the same image 
	    for(int n = 0; n < results.length(); n++){
	
	    	int id = results.getJSONObject(n).getInt("id");
	    	String name = results.getJSONObject(n).getString("employee_name");
	    	int salary = results.getJSONObject(n).getInt("employee_salary");
	    	
	    	System.out.println("Id: " + id + ", Employee Name: " + name + " , salary: " + salary);
	    	
	    	if(id == 11){
	    		Assert.assertEquals(name, "Jena Gaines");
	    		Assert.assertEquals(salary, 90560);
	    	}
	
	    }       
	
	}

	/*
	 *  Get One Employee data by EmpID, and verify the employee's record
	 */
	@Test
	public void verifyEmployeeById() {
		
		RestAssured.baseURI = Public_URL + "/api/v1/employee/11";
		
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON);
		//httpRequest.param("id", "123");
	
		Response response = httpRequest.get();
		
		//System.out.println(response.asString());
		
		// Start parsing
	    JSONObject obj = new JSONObject(response.asString());
	    //System.out.println(obj.toString());
	    
	    // get and verify response status
	    String status = obj.getString("status");
	    Assert.assertEquals(status, "success");
	    
	    if(status.equalsIgnoreCase("success")){
	    	
	    	String name = obj.getJSONObject("data").getString("employee_name");
    		Assert.assertEquals(name, "Jena Gaines");

    		int salary = obj.getJSONObject("data").getInt("employee_salary");
    		Assert.assertEquals(salary, 90560);
    		
	    	System.out.println("Employee Name: " + name + " , salary: " + salary);
	    }
	    System.out.println("status: "+ status);
	
	}
	
	//@Test
	public static void createEmployeeRecordTest() throws IOException {
		
		URL url = null;
		try {
			url = new URL("http://dummy.restapiexample.com/api/v1/create");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("id", "9408501"); // set userId its a sample here
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");
		
		int responseCode = http.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	        		http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

	            // print result
			System.out.println(response.toString());
		}else{
			System.out.println("POST NOT WORKED");
		}

		String data = "{\"status\": \"success\",\"data\": {\"name\": \"sf,bay\",\"salary\": \"12121\",\"age\": \"23\",\"id\": 9408501}}";
				//"{\n  \"Id\": 78912,\n  \"Customer\": \"Jason Sweet\",\n  \"Quantity\": 1,\n  \"Price\": 18.00\n}";

		byte[] out = data.getBytes(StandardCharsets.UTF_8);

		OutputStream stream = http.getOutputStream();
		stream.write(out);

		System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
		http.disconnect();

	}
	
	
}
