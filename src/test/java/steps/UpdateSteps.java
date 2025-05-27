package steps;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import main.ConfigReader;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

public class UpdateSteps {

    WebDriver driver;
    LoginPage loginPage;

    @Given("I open the Payflex login page")
    public void i_open_the_payflex_login_page() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://customer.uat.payflex.co.za/"); // replace with the actual login URL
    }

    @When("I enter valid credentials")
    public void i_enter_valid_credentials() throws InterruptedException {
        String username = ConfigReader.get("email");
        String password = ConfigReader.get("password");

        driver.findElement(By.id("email")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("/html/body/app-root/div/div[1]/div/app-login-customer/div/div/div/div[2]/button[1]")).click(); // update to your real button ID
        driver.wait(6000);
    }

    public void loginViaAPI() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String usernameStr = ConfigReader.get("email");
        String passwordStr = ConfigReader.get("password");
        String jsonBody = new ObjectMapper().writeValueAsString(new HashMap<>() {{
            put("grant_type", "password");
            put("username", usernameStr);
            put("password", passwordStr);
            put("audience", "https://auth-dev.payflex.co.za");
            put("client_id", "YOUR_CLIENT_ID");
            put("client_secret", "YOUR_CLIENT_SECRET");
        }});

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://payflex.eu.auth0.com/oauth/token"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String accessToken = new ObjectMapper().readTree(response.body()).get("access_token").asText();

        driver.get("https://customer.uat.payflex.co.za/");
        String script = String.format("window.localStorage.setItem('access_token', '%s');", accessToken);
        ((JavascriptExecutor) driver).executeScript(script);

        driver.navigate().to("https://customer.uat.payflex.co.za/dashboard");
    }

    @Then("I should be logged in and see my dashboard")
    public void i_should_be_logged_in_and_see_my_dashboard() throws InterruptedException {

        WebElement dashboard = driver.findElement(By.xpath("//*[contains(text(),'Dashboard')]"));
        assert dashboard.isDisplayed() : "Dashboard not visible!";
    }

    @And("I select edit for profile details")
    public void iSelectEditForProfileDetails() throws InterruptedException {
        driver.findElement(By.xpath("/html/body/app-root/div/div[1]/div/app-profile-customer/div/div/div/div/div[1]/div/div/p[4]/button")).click();
    }


    @Then("I update my address city {string} and postal {string}")
    public void iUpdateMyAddressCityAndPostal(String city, String postal)
    {
        driver.findElement(By.id("customerAddressCity")).clear();
        driver.findElement(By.id("customerAddressPostal")).clear();
        driver.findElement(By.id("customerAddressCity")).sendKeys(city);
        driver.findElement(By.id("customerAddressPostal")).sendKeys(postal);
        driver.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-pp-modal-account/div/div/div[1]/form/div[6]/button")).click();
    }

    @Then("I validate the success message")
    public void iValidateTheSuccessMessage()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3000));

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/div/app-toast/ngb-toast/div/div"))
        );
        WebElement successMessage = driver.findElement(By.xpath("/html/body/app-root/div/app-toast/ngb-toast/div/div"));

        assert successMessage.isDisplayed() : "Success message not visible!";
        assert successMessage.getText().contains("Address updated successfully") : "Success message text mismatch!";
    }

    @When("I login via the API")
    public void iLoginViaTheAPI() throws Exception {
        loginViaAPI();
    }
}
