package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Payflex Customer Portal Test Report");
            sparkReporter.config().setReportName("Login Automation Tests");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }
}
