package com.mop.qa.test.regression;

import org.testng.annotations.Test;

import com.mop.qa.Utilities.CSVUtil;
import com.mop.qa.pageobject.NFT_ServicePortal;
import com.mop.qa.testbase.TestBase;

/**
 * Purpose: Login: As an existing SSP user, Need to login into the SSP portal
 * 
 * @version 1.0 Use Case for Web
 *  Date Modified - 23 Dec 2019
 */
public class Test_NFT_S05_O2A_Stream_user_Current_Test_Results extends TestBase {
	@SuppressWarnings("static-access")
	@Test
	public void Test_NFT_S05_O2A_Stream_user_TestRun_TestCase() throws Exception {
		try {
			/*String URL = rds.getValue("DATA", currentTest, "URL");
			String UserName = rds.getValue("DATA", currentTest, "UserName");
			String Password = rds.getValue("DATA", currentTest, "Password");*/
			
			CSVUtil rdss = new CSVUtil();
			System.out.println("Get Data from CSV File!!!");
            String URL = rdss.readCSVFilewitRowName("URL", "S05_O2A_Stream_user_Current_Test_Results-Run1");
            String username = rdss.readCSVFilewitRowName( "UserName", "S05_O2A_Stream_user_Current_Test_Results-Run1");
            String password = rdss.readCSVFilewitRowName( "Password", "S05_O2A_Stream_user_Current_Test_Results-Run1");
                   
			NFT_ServicePortal NFTobj = null;
			NFTobj = selectToolNFT();
			
			
			/*
			 * Scenario 1: Step 1- Login: As an existing SSP user, I need to login into the SSP portal
			 */
			
			NFTobj.loginAsExistingUser(URL, username, password);
			NFTobj.O2A_Stream_Current_Test_Results();
			NFTobj.logout();
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
