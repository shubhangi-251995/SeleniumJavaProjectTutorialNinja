package tutorialsNinja.register;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Date;

@Test
public class TC_01 {
    public void verifyRegistrationWithManditoryFields() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://tutorialsninja.com/demo");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//span[text()='My Account']")).click();
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("input-firstname")).sendKeys("Robby");
        driver.findElement(By.id("input-lastname")).sendKeys("Jesus");
        driver.findElement(By.name("email")).sendKeys(generateNewEmail());
        driver.findElement(By.name("telephone")).sendKeys("1234567890");
        driver.findElement(By.name("password")).sendKeys("RobbyJesus@2026");
        driver.findElement(By.name("confirm")).sendKeys("RobbyJesus@2026");
        driver.findElement(By.name("newsletter")).click();
        driver.findElement(By.name("agree")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@class='btn btn-primary']")).click();
//        dr.quit();

        Assert.assertTrue(driver.findElement(By.linkText("Logout")).isDisplayed());
        String accountSuccessMessage="Your Account Has Been Created!";
        Assert.assertEquals(driver.findElement(By.xpath("//div[@id='common-success']//h1")).getText(),accountSuccessMessage);
        String ActualProperDetails1= "Congratulations! Your new account has been successfully created!";
        String ActualProperDetails2= "You can now take advantage of member privileges to enhance your online shopping experience with us.";
        String ActualProperDetails3= "If you have ANY questions about the operation of this online shop, please e-mail the store owner.";
        String ActualProperDetails4= "A confirmation has been sent to the provided e-mail address. If you have not received it within the hour, please contact us.";

        String expectedProperDetails= driver.findElement(By.id("content")).getText();
        Assert.assertTrue(expectedProperDetails.contains(ActualProperDetails1));
        Assert.assertTrue(expectedProperDetails.contains(ActualProperDetails2));
        Assert.assertTrue(expectedProperDetails.contains(ActualProperDetails3));
        Assert.assertTrue(expectedProperDetails.contains(ActualProperDetails4));
        driver.findElement(By.linkText("Continue")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Account']")).isDisplayed());
        System.out.println("code is executed successfully");
        driver.quit();
    }

    public String generateNewEmail() {
        //generate email at run time
        Date date = new Date();
        System.out.println(date);
        String dateString = date.toString();
        String DateWithoutSpaces = dateString.replaceAll(" ", "").replaceAll(":", "");
        String EmailAddress = DateWithoutSpaces + "@gmail.com";
        System.out.println(EmailAddress);
        return EmailAddress;

    }


}
