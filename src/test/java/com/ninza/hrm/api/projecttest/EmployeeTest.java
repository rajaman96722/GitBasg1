package com.ninza.hrm.api.projecttest;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.Driver;
import com.ninza.hrm.api.pojoclasses.ProjectPojo;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class EmployeeTest {
	
	 ProjectPojo pObj;
	@Test
	
	public void addSingleProjectWithCreatedtest() throws SQLException {
		

		Random ran = new Random();
	   int ranNum=	ran.nextInt(5000);
	   String  projectName ="ABB_"+ranNum;
	   
	   String expSucMsg= "Successfully Added";
		  pObj= new ProjectPojo(projectName,"created","AmanRaj", 10);
	//verify the projectName In Api Layer
		 
     Response resp=	given().contentType(ContentType.JSON)
	 .body(pObj)
	 .when().post("http://49.249.28.218:8091/addProject");
	resp.then().assertThat().statusCode(201).assertThat().time(Matchers.lessThan(5000L))
	 .assertThat().contentType(ContentType.JSON).log().all();
	
	 String actMsg=resp.jsonPath().get("msg");
	 Assert.assertEquals(expSucMsg, actMsg);
	 //verify the projectName in Db Layer
	 boolean flag = false;
	  Driver driverRef = new Driver();
	  DriverManager.registerDriver(driverRef);
	     Connection con=  DriverManager.getConnection("jdbc:mysql://49.249.28.218:3306/ninza_hrm", "root@%", "root");
	         ResultSet result  = con.createStatement().executeQuery("select * from project");
	        while(result.next()){
	        	 if(result.getString(4).equals(projectName)) {
	        		 flag = true;
	        		 break;
	        		 
	        	 }
	         }
	         con.close();
	         Assert.assertEquals(flag, "project in DB is not verified");
	
	}
	@Test(dependsOnMethods="addSingleProjectWithCreatedtest()")
	public void createDuplicateProjecttest() {

		Random ran = new Random();
			given().contentType(ContentType.JSON)
				 .body(pObj)
				 .when().post("http://49.249.28.218:8091/addProject")
			.then().assertThat().statusCode(409)
			.log().all();
		
}
}




