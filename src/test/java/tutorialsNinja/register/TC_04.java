package tutorialsNinja.register;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

@Test
public class TC_04 {
    /*
     Verify proper notification messages are displayed for the mandatory fields when user don't provide any fields in the 'register account' page and submit.
    */
    public void validateErrorMessages() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver= new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();
        driver.get("https://tutorialsninja.com/demo");
        driver.findElement(By.xpath("//span[@class='caret']")).click();
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        String firstNameErrorMessage= "First Name must be between 1 and 32 characters!";
        String lastNameErrorMessage= "Last Name must be between 1 and 32 characters!";
        String emailErrorMessage= "E-Mail Address does not appear to be valid!";
        String telephoneErrorMessage= "Telephone must be between 3 and 32 characters!";
        String passwordErrorMessage= "Password must be between 4 and 20 characters!";
        String privacyPolicyrrorMessage="Warning: You must agree to the Privacy Policy!";
        Assert.assertEquals(driver.findElement(By.xpath("//input[@id='input-firstname']/following-sibling::div")).getText(),firstNameErrorMessage);
        Assert.assertEquals(driver.findElement(By.xpath("//input[@id='input-lastname']/following-sibling::div")).getText(),lastNameErrorMessage);
        Assert.assertEquals(driver.findElement(By.xpath("//input[@id='input-email']/following-sibling::div")).getText(),emailErrorMessage);
        Assert.assertEquals(driver.findElement(By.xpath("//input[@id='input-telephone']//following-sibling::div")).getText(),telephoneErrorMessage);
        Assert.assertEquals(driver.findElement(By.xpath("//input[@id='input-password']//following-sibling::div")).getText(),passwordErrorMessage);
//        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='alert alert-danger alert-dismissible']")).getText(),privacyPolicyrrorMessage);
        String privacyPolicy=driver.findElement(By.xpath("//div[@class='alert alert-danger alert-dismissible']")).getText();
        System.out.println("privacyPolicy----"+privacyPolicy);
        driver.quit();

    }
}
