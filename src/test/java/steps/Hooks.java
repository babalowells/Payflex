package steps;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ExtentManager;
import utils.DriverManager;

import java.util.Base64;

public class Hooks {
    public static ExtentReports extent;
    public static ExtentTest test;

    @Before
    public void beforeScenario(Scenario scenario) {
        extent = ExtentManager.getInstance();
        test = extent.createTest(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
            test.fail("Scenario failed",
                    com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        extent.flush();
    }
}
