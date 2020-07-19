package core;

import org.influxdb.dto.Point;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.TimeUnit;

public class InfluxDBListener implements ITestListener {
	
	private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

  public void onTestStart(ITestResult iTestResult) {
	  System.out.println();
	  System.out.println("onTestStart: " + getTestMethodName(iTestResult));

  }

  public void onTestSuccess(ITestResult iTestResult) {
	  System.out.println("onTestSuccess: " + getTestMethodName(iTestResult) + " PASS\n");
	  this.postTestMethodStatus(iTestResult, "PASS");
  }

  public void onTestFailure(ITestResult iTestResult) {
	  System.out.println("onTestFailure: " + getTestMethodName(iTestResult) + " FAIL\n");
	  this.postTestMethodStatus(iTestResult, "FAIL");
  }

  public void onTestSkipped(ITestResult iTestResult) {
	  System.out.println("onTestSkipped: " + getTestMethodName(iTestResult) + " SKIPPED\n");
	  this.postTestMethodStatus(iTestResult, "SKIPPED");
  }

  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

  }

  public void onStart(ITestContext iTestContext) {
	  System.out.println();
	  System.out.println("TESTS START: ");
	  System.out.println();
  }

  public void onFinish(ITestContext iTestContext) {
	  System.out.println();
	  System.out.println("TESTS COMPLETED. ");
	  System.out.println();
	  this.postTestClassStatus(iTestContext);
  }

  private void postTestMethodStatus(ITestResult iTestResult, String status) {
    Point point = Point.measurement("testmethod").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .tag("testclass", iTestResult.getTestClass().getName()).tag("name", iTestResult.getName())
        .tag("description", iTestResult.getMethod().getDescription()).tag("result", status)
        .addField("duration", (iTestResult.getEndMillis() - iTestResult.getStartMillis())).build();
    
    InfluxConnectorSingleton.getInstance().postInfluxDataPoint(point);
    
  }

  private void postTestClassStatus(ITestContext iTestContext) {
    Point point = Point.measurement("testclass").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .tag("name", iTestContext.getAllTestMethods()[0].getTestClass().getName())
        .addField("duration", (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()))
        .build();
    
    InfluxConnectorSingleton.getInstance().postInfluxDataPoint(point);
    
  }

}