package getRequest;

import org.testng.Assert;
import org.testng.annotations.Test;

//import groovyjarjarantlr.collections.List;

import java.util.Map;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetData {
	
	final static String url = "https://istheapplestoredown.com/api/v1/status/worldwide";
	final static String [] countries = { "ae", "at", "au", "be-fr", "be-nl", "br", "ca", 
						"ch-de", "ch-fr","cn","cz","de","dk","es", "fi", "fr", "hk", 
						"hk-zh", "hu", "id", "ie", "it", "jp", "kr", "lu", "mx", "my", 
						"nl","no", "nz", "ph", "pl", "pt", "ru", "se", "sg", "th",  
						"th-en", "tr", "tw", "uk", "us", "vn", "xf" };
	@Test
	public void testResponsecode(){
		Response resp = RestAssured.get( url );
		// get and verify the status code
		int code = resp.getStatusCode();
		Assert.assertEquals( code, 200 );
	}

	@Test
	public void testExtractDetails(){
		
		Response resp = RestAssured.get( url );
		
		// check for each countries 
		for( String country : countries ){
			// get the country's record
			Map<String, String> map = resp.jsonPath().get( country );
			// check if the status is 'y'
			if( map.get("status").equals("y") ){
				// display the name of the country
				System.out.println( "Country name is : " + map.get( "name" ) );
				// Fail the test
				Assert.assertEquals( "n", "y" );
			}
		}
	}
	
//	public void testExtractDetails2(){
//		
//		String resp = RestAssured.get( url ).asString();
//		List<String> list = from(resp).getList("findAll {it.status.equals("y")}.name");
//		
//	}
		

}