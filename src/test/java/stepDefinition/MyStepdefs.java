package stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MyStepdefs {
    WebDriver driver;
    private Assert Assertions;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Given("I am on the registration page")
    public void i_am_on_the_registration_page() {
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    @When("I fill in all required fields correctly")
    public void i_fill_in_all_required_fields_correctly() {
        driver.findElement(By.id("dp")).sendKeys("10/04/1952");
        driver.findElement(By.id("member_firstname")).sendKeys("Test");
        driver.findElement(By.id("member_lastname")).sendKeys("User");

        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        driver.findElement(By.id("member_emailaddress")).sendKeys(email);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(email);

        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Password123");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Password123");
    }

    @When("I accept all required terms")
    public void i_accept_all_required_terms() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement terms = driver.findElement(By.id("sign_up_25"));
        js.executeScript("arguments[0].scrollIntoView(true);", terms);
        js.executeScript("arguments[0].click();", terms);

        WebElement conduct = driver.findElement(By.id("sign_up_26"));
        js.executeScript("arguments[0].click();", conduct);

        WebElement ethics = driver.findElement(By.id("fanmembersignup_agreetocodeofethicsandconduct"));
        js.executeScript("arguments[0].click();", ethics);
    }

    @When("I do not accept the terms")
    public void i_do_not_accept_the_terms() {
        // Intentionally left blank
    }

    @Then("I click the {string} button")
    public void i_click_the_button(String buttonLabel) {
        try {
            if (buttonLabel.equalsIgnoreCase("Confirm and Join")) {
                WebElement confirmButton = driver.findElement(By.xpath("//input[@type='submit' and @name='join' and @value='CONFIRM AND JOIN']"));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(confirmButton));

                confirmButton.click();
            } else {
                System.out.println("Button with label '" + buttonLabel + "' is not handled.");
            }
        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Failed to find or click the '" + buttonLabel + "' button: " + e.getMessage());
            throw e;
        }
    }

    @Then("I should see a success or confirmation message")
    public void i_should_see_a_success_or_confirmation_message() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            boolean onConfirmationPage = wait.until(ExpectedConditions.urlContains("SignUpConfirmation"));
            System.out.println("Redirected to confirmation page.");
            assertTrue("Not redirected to confirmation page.", onConfirmationPage);

        } catch (TimeoutException e) {
            System.out.println("No redirect to confirmation page. Trying fallback...");

            try {
                WebElement confirmationMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(), 'Thank you') or contains(text(), 'successfully')]")
                ));
                System.out.println("Confirmation message found: " + confirmationMsg.getText());
                Assertions.assertNotNull(String.valueOf(confirmationMsg), "Confirmation message not found.");

            } catch (TimeoutException ex) {
                System.out.println("Confirmation message not found.");
                System.out.println("Current URL: " + driver.getCurrentUrl());

                List<WebElement> errors = driver.findElements(By.className("field-validation-error"));
                for (WebElement error : errors) {
                    System.out.println("Form error: " + error.getText());
                }

                Assert.fail("Neither confirmation page nor message found.");
            }
        }
    }

    @When("I fill in the form without a last name")
    public void i_fill_in_the_form_without_a_last_name() {
        driver.findElement(By.id("dp")).sendKeys("16/06/1992");
        driver.findElement(By.id("member_firstname")).sendKeys("Helena");

        String email = "idontknowwhatimdoing@example.com";
        driver.findElement(By.id("member_emailaddress")).sendKeys(email);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(email);

        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Password123");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Password123");
    }

    @Then("I should see an error for missing last name")
    public void i_should_see_an_error_for_missing_last_name() {
        WebElement error = driver.findElement(By.id("member_lastname__label"));
        Assert.assertTrue(error.isDisplayed());
    }

    @When("I fill in the form with mismatched passwords")
    public void i_fill_in_the_form_with_mismatched_passwords() {
        driver.findElement(By.id("dp")).sendKeys("05/01/2000");
        driver.findElement(By.id("member_firstname")).sendKeys("Jacke");
        driver.findElement(By.id("member_lastname")).sendKeys("Sparrow");

        String email = "IHaveAJarFullOfDirt@example.com";
        driver.findElement(By.id("member_emailaddress")).sendKeys(email);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(email);

        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Password123");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Password321");
    }

    @Then("I should see an error for password mismatch")
    public void i_should_see_an_error_for_password_mismatch() {
        WebElement error = driver.findElement(By.id("signupunlicenced_confirmpassword"));
        Assert.assertTrue(error.isDisplayed());
    }

    @Then("I should see an error for unaccepted terms")
    public void i_should_see_an_error_for_unaccepted_terms() {
        WebElement error = driver.findElement(By.id("generalErrors"));
        Assert.assertTrue(error.isDisplayed());
    }

    @After
    public void tearDown() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (driver != null) {
            driver.quit();
        }
    }

}
