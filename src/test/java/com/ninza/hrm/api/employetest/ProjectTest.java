package com.ninza.hrm.api.employetest;

import static io.restassured.RestAssured.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.Driver;
import com.ninza.hrm.api.pojoclasses.EmployeePojo;
import com.ninza.hrm.api.pojoclasses.ProjectPojo;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class ProjectTest {
	
	@Test
	public void addEmployeeTest() throws SQLException {
		
			Random ran = new Random();
			int ranNum = ran.nextInt(5000);
			String projName="Airtel_"+ranNum;
			String username = "user"+ranNum;
			// Api-1==> add a project in side server
			ProjectPojo pObj = new ProjectPojo(projName, "created", "AmanRaj", 10);

			 given().contentType(ContentType.JSON).body(pObj).when()
					.post("http://49.249.28.218:8091/addProject")
			.then().log().all();
			
			// Api-2==> add employee to same project

			EmployeePojo empObj = new EmployeePojo("Architect", "03/11/1995", "rajaman96722@gmail.com", "AmanRaj" + ranNum,
					18, "8871514233", projName, "ROLE_ADMIN", "AmanRaj" + username);

			given().contentType(ContentType.JSON).body(empObj).when().post("http://49.249.28.218:8091/employees").then()
					.assertThat().statusCode(201).assertThat().time(Matchers.lessThan(5000L))
					.log().all();
			//verify emp name in DB
			boolean flag=false;
			 Driver driverRef = new Driver();
			  DriverManager.registerDriver(driverRef);
			     Connection con=  DriverManager.getConnection("jdbc:mysql://49.249.28.218:3306/ninza_hrm", "root@%", "root");
			         ResultSet result  = con.createStatement().executeQuery("select * from employee");
			        while(result.next()){
			        	 if(result.getString(5).equals(username)) {
			        		 flag = true;
			        		 break;
			        		 
			        	 }
			         }
			         con.close();
			         Assert.assertEquals(flag, "project in DB is not verified");
			
			}
		}
	      
	


