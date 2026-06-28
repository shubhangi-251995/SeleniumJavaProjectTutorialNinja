package tutorialsNinja.register;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.sql.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;


@Test
public class TC_03 {
    public void registerAccountWithAllDetails() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();
        driver.get("https://tutorialsninja.com/demo");
        driver.findElement(By.xpath("//span[@class='caret']")).click();
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("input-firstname")).sendKeys("Robby");
        driver.findElement(By.id("input-lastname")).sendKeys("Jesus");
        driver.findElement(By.name("email")).sendKeys(TC_01.generateNewEmail());
        driver.findElement(By.id("input-telephone")).sendKeys("1234567890");
        driver.findElement(By.id("input-password")).sendKeys("RobbyJesus@2026");
        driver.findElement(By.id("input-confirm")).sendKeys("RobbyJesus@2026");
        driver.findElement(By.xpath("//label[text()='Yes']//input")).click();
        driver.findElement(By.name("agree")).click();
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//div[@id='common-success']//h1")).getText(), "Your Account Has Been Created!");
        String actualMessage = driver.findElement(By.id("content")).getText();
        Assert.assertTrue(actualMessage.contains("Congratulations! Your new account has been successfully created!"));
        Assert.assertTrue(actualMessage.contains("You can now take advantage of member privileges to enhance your online shopping experience with us."));
        Assert.assertTrue(actualMessage.contains("If you have ANY questions about the operation of this online shop, please e-mail the store owner."));
        Assert.assertTrue(actualMessage.contains("A confirmation has been sent to the provided e-mail address. If you have not received it within the hour, please contact us."));
       Assert.assertTrue(driver.findElement(By.linkText("Continue")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.linkText("Logout")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//ul[@class='breadcrumb']//a[text()='Success']")).isDisplayed());
    }
}
