package com.mop.qa.testrunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.mop.qa.Utilities.CSVUtil;
import com.mop.qa.Utilities.TestParameter;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;


public class TestRunner {
	static List<TestParameter> list;
	public static List<String> testname;
	public static void main(String args[]) {

/*	@Test
	public void testRun() {*/
		try {
			boolean flag = true;
			TestNG testNG = new TestNG();
			String toolType = null;
			String executionType = null;

			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			
			//testNG.addExecutionListener(new TestSuite());
			List<List<TestParameter>> testLists = testList();
			XmlSuite suite1 = new XmlSuite();
			suite1.setName("Suite_seq");

			for (int j = 0; j < testLists.size(); j++) {
				List<XmlTest> tests = new ArrayList<XmlTest>();
				List<XmlTest> tests1 = new ArrayList<XmlTest>();
				List<XmlTest> tests2 = new ArrayList<XmlTest>();
				List<TestParameter> suiteTest = testLists.get(j);
				XmlSuite parasuite = new XmlSuite(); 
				parasuite.setName("cross_para");
				XmlSuite suite = new XmlSuite();
				suite.setName("Suite_para" + 1);
				System.out.println("run");
				for (int i = 0; i < suiteTest.size(); i++) {
					XmlTest test = new XmlTest(suite);
					List<XmlClass> xmlclass = new ArrayList<XmlClass>();

					test.addParameter("testname", suiteTest.get(i).getTestName());
					test.addParameter("testname1", suiteTest.get(i).getTestName1());
					if(suiteTest.get(i).getToolName().equalsIgnoreCase("Connected Device")){
						if(flag){
						test.addParameter("toolName", suiteTest.get(i).getToolName());
						xmlclass.add(new XmlClass("STBT_Roku.TestRun"));
						test.setXmlClasses(xmlclass);
						tests1.add(test);
						suite1.setTests(tests1);
						flag=false;
						}
					} 
					else{
					xmlclass.add(new XmlClass(suiteTest.get(i).getTestClass()));
					test.setName(suiteTest.get(i).getTestName());
					test.setXmlClasses(xmlclass);
					test.addParameter("testname", suiteTest.get(i).getTestName());
					test.addParameter("testname1", suiteTest.get(i).getTestName1());
					test.addParameter("toolName", suiteTest.get(i).getToolName());
					test.addParameter("browser", suiteTest.get(i).getBrowser());
					test.addParameter("appType", suiteTest.get(i).getAppType());
					test.addParameter("ExecutionType", suiteTest.get(i).getExecutionType());
					test.addParameter("Locality", suiteTest.get(i).getLocality());
					toolType = suiteTest.get(i).getToolName();
					executionType = suiteTest.get(i).getExecutionType();
					System.out.println("exe type" + executionType);
					if (suiteTest.get(i).getLocality().equalsIgnoreCase("Grid")||suiteTest.get(i).getLocality().equalsIgnoreCase("Cloud")) {
						test.addParameter("RemoteUrl", suiteTest.get(i).getRemoteUrl());
					}
					if (executionType.equalsIgnoreCase("Parallel")) {
						if (toolType.equalsIgnoreCase("Selenium")) {
							if(Integer.parseInt( suiteTest.get(i).getNo_Of_Instances())<=1){
								System.out.println("1");
								tests2.add(test);
							} 
							else if (((i + 1) < suiteTest.size())
									&& (suiteTest.get(i).getTestName1().equals(suiteTest.get(i + 1).getTestName1()))) {
								tests.add(test);
								suite.setParallel("tests");
								suite.setThreadCount(Integer.parseInt(suiteTest.get(i).getNo_Of_Instances()));
							} else {
								tests.add(test);
								suite.setTests(tests);
								suite.setParallel("tests");
								suite.setThreadCount(Integer.parseInt(suiteTest.get(i).getNo_Of_Instances()));
								suites.add(suite);
								tests = new ArrayList<XmlTest>();
								suite = new XmlSuite();
								suite.setName("Suite_para" + i);
							}
						} else if (toolType.equalsIgnoreCase("Appium")) {
							if(Integer.parseInt( suiteTest.get(i).getNo_Of_Instances())<=1){
								test.addParameter("platformName", suiteTest.get(i).getPlatformName());
								test.addParameter("udid", suiteTest.get(i).getUdid());
								System.out.println("1");
								tests2.add(test);
							} 
							else if (((i + 1) < suiteTest.size())
									&& (suiteTest.get(i).getTestName1().equals(suiteTest.get(i + 1).getTestName1()))) {
								test.addParameter("platformName", suiteTest.get(i).getPlatformName());
								test.addParameter("udid", suiteTest.get(i).getUdid());
								tests.add(test);
								System.out.println("adding test");
								suite.setTests(tests);
								suite.setParallel("tests");
								suite.setThreadCount(tests.size());
							} else {
								test.addParameter("platformName", suiteTest.get(i).getPlatformName());
								test.addParameter("udid", suiteTest.get(i).getUdid());
								//tests.add(test);
								suite.setTests(tests);
								suite.setParallel("tests");
								suite.setThreadCount(Integer.parseInt(suiteTest.get(i).getNo_Of_Instances()));
								suites.add(suite);
								tests = new ArrayList<XmlTest>();
								suite = new XmlSuite();
								suite.setName("Suite_para"+(i+1));
							}
						} else {
							tests.add(test);
						}

					} else if (executionType.equalsIgnoreCase("Sequential")) {
						if (toolType.equalsIgnoreCase("Selenium")) {
							tests1.add(test);
						} else if (toolType.equalsIgnoreCase("Appium")) {
							test.addParameter("platformName", suiteTest.get(i).getPlatformName());
							test.addParameter("udid", suiteTest.get(i).getUdid());
							tests1.add(test);
						} else {
							tests.add(test);
						}
						suite1.setTests(tests1);
						
					}
				}
				}
				parasuite.setTests(tests2);
				parasuite.setParallel("tests"); 
				suites.add(parasuite); 
				//suite.setTests(tests);
				suites.add(suite1);
				for (XmlSuite s : suites)
					System.out.println(s.toXml());

			}
			testNG.setXmlSuites(suites);
			testNG.run();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

}


	public XmlSuite getXmlSuite() {
		XmlSuite suite = new XmlSuite();
		suite.setName("TestSuite");
		return suite;
	}

	public static List<List<TestParameter>> testList() {

		try {
			List<List<TestParameter>> testRunner = new ArrayList<List<TestParameter>>();
			FileInputStream fis = new FileInputStream("./TestRunnerUAF.csv");
			 CsvParserSettings csvParserSettings = new CsvParserSettings();
//             csvParserSettings.selectFields("TestClass");
             csvParserSettings.setHeaderExtractionEnabled(true);
             
             csvParserSettings.getFormat().setDelimiter('|');
             
             csvParserSettings.getFormat().setLineSeparator("\n");
             CsvParser parser = new CsvParser(csvParserSettings);
             System.out.println("Read the data");
             List<String[]> rowCount = parser.parseAll(new File("./TestRunnerUAF.csv"));
             System.out.println(rowCount.size());
			boolean flag = true;
			String[] platform = null;
			String[] browser = null;
			testname = new ArrayList<String>();

				list = new ArrayList<TestParameter>();
				FileReader reader = new FileReader("TestRunnerUAF.csv");
				 parser.beginParsing(reader);
                Record record;
				  while ((record = parser.parseNextRecord()) != null) {
					
                   String[] recordValues=record.getString(0).split(",");
					System.out.println("Get the first record ");
					
					
					if (recordValues[2].toString().equalsIgnoreCase("Yes")) {
						if(!recordValues[6].toString().equalsIgnoreCase("Connected Device")){
							 browser = recordValues[9].toString().split("-");
							 platform = recordValues[8].toString().split("-"); 
						}
						int count1 = 1;
						for (int l = 0; l < count1 ; l++) {
							TestParameter test = new TestParameter();
							test.setTestName1(recordValues[0].toString());
							String name = recordValues[0].toString();
							test.setTestName(name + "-Run" + (l+1));
							if(recordValues[6].toString().equalsIgnoreCase("Connected Device")){
								
									System.out.println("flag-"+flag);
								test.setToolName(recordValues[6].toString());
								test.setTestName(name + "-Run" + (l+1));
								testname.add(recordValues[1].toString());
							
							}
							else{
							test.setTestClass(recordValues[1].toString());
							test.setExecutionType(recordValues[3].toString());
							test.setNo_Of_Instances(recordValues[4].toString());
							if (Integer.parseInt(test.getNo_Of_Instances()) > count1) {
								count1 = Integer.parseInt(test.getNo_Of_Instances());
							}
							test.setLocality(recordValues[5].toString());
							test.setToolName(recordValues[6].toString());
							test.setAppType(recordValues[7].toString());
							if (test.getToolName().equalsIgnoreCase("Appium")) {
								if (platform.length < Integer.parseInt(test.getNo_Of_Instances())) {
									test.setPlatformName(platform[0]);
								} else {
									test.setPlatformName(platform[l]);
								}
								//test.setPlatformName(recordValues(8).toString());
								String udid[] = recordValues[10].toString().split("-");
								test.setUdid(udid[l]);
							}
							if (recordValues[5].toString().equalsIgnoreCase("Grid")||recordValues[5].toString().equalsIgnoreCase("Cloud")) {
								test.setRemoteUrl(recordValues[11 + l].toString());
							}
							if (browser.length < Integer.parseInt(test.getNo_Of_Instances())) {
								test.setBrowser(browser[0]);
							} else {
								test.setBrowser(browser[l]);
							}
							}
							list.add(test);
						}
						System.out.println("testnames"+testname);
					}
					
	/*				

					if(!record.getString(6).toString().equalsIgnoreCase("Connected Device")){
						 browser = record.getString(9).toString().split("-");
						 platform = record.getString(8).toString().split("-"); 
					}
					int count1 = 1;
					for (int l = 0; l < count1 ; l++) {
						TestParameter test = new TestParameter();
						test.setTestName1(record.getString(0).toString());
						String name = record.getString(0).toString();
						test.setTestName(name + "-Run" + (l+1));
						if(record.getString(6).toString().equalsIgnoreCase("Connected Device")){
							
								System.out.println("flag-"+flag);
							test.setToolName(record.getString(6).toString());
							test.setTestName(name + "-Run" + (l+1));
							testname.add(record.getString(1).toString());
						
						}
						else{
						test.setTestClass(record.getString(1).toString());
						test.setExecutionType(record.getString(3).toString());
						test.setNo_Of_Instances(record.getString(4).toString());
						if (Integer.parseInt(test.getNo_Of_Instances()) > count1) {
							count1 = Integer.parseInt(test.getNo_Of_Instances());
						}
						test.setLocality(record.getString(5).toString());
						test.setToolName(record.getString(6).toString());
						test.setAppType(record.getString(7).toString());
						if (test.getToolName().equalsIgnoreCase("Appium")) {
							if (platform.length < Integer.parseInt(test.getNo_Of_Instances())) {
								test.setPlatformName(platform[0]);
							} else {
								test.setPlatformName(platform[l]);
							}
							//test.setPlatformName(record.getString(8).toString());
							String udid[] = record.getString(10).toString().split("-");
							test.setUdid(udid[l]);
						}
						if (record.getString(5).toString().equalsIgnoreCase("Grid")||record.getString(5).toString().equalsIgnoreCase("Cloud")) {
							test.setRemoteUrl(record.getString(11 + l).toString());
						}
						if (browser.length < Integer.parseInt(test.getNo_Of_Instances())) {
							test.setBrowser(browser[0]);
						} else {
							test.setBrowser(browser[l]);
						}
						}
						list.add(test);
					}
					System.out.println("testnames"+testname);*/
				
				}
				testRunner.add(list);
			return testRunner;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	public static String getAppProperties(String key) throws IOException {
		String value = "";
		try {

			FileInputStream fileInputStream = new FileInputStream("data.properties");
			Properties property = new Properties();
			property.load(fileInputStream);

			value = property.getProperty(key);

			fileInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}

}

