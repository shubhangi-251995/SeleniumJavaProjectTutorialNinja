package tutorialsNinja.register;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TC_02 {
    /**
     * Verify "Thank you for registering" email is sent to the registered email address as a confirmation for registering the account.
     * This feature is not configured with the tutorialNinja application , so that automation is not possible for this test case.
     * The same test case we can automate for another application i.e. Amazon.in where if the password the forgotten then the reset password email  should be sent after
     * entering a valid registered email address
     */
@Test
    public void verifyEmailConfirmation() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.amazon.in/");
        List<WebElement> continueButtons =
                driver.findElements(By.xpath("//button[@type='submit']"));

        if (!continueButtons.isEmpty()
                && continueButtons.get(0).isDisplayed()
                && continueButtons.get(0).isEnabled()) {

            System.out.println("Continue button is displayed and enabled");
            continueButtons.get(0).click();

        } else {

            System.out.println("Continue button is not displayed");
        }

        System.out.println("Further execution continues...");
        String eMail = "shubhangikadam.sk25@gmail.com";
        String appPassword = "pwuc gsgj czdy hnwu";
        driver.findElement(By.xpath("//span[text()='Hello, sign in']")).click();
        driver.findElement(By.id("ap_email_login")).sendKeys(eMail);

        driver.findElement(By.xpath("//input[@type='submit']")).click();
        driver.findElement(By.xpath("//a[text()='Forgot password?']")).click();

        WebElement email = driver.findElement(By.xpath("//input[@type='email']"));
        email.click();
        email.clear();
        email.sendKeys(eMail);

        driver.findElement(By.xpath("//input[@type='submit']")).click();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//         Gmail IMAP configuration/settings
        // Gmail IMAP server details used to establish a secure connection with Gmail mailbox
        String host = "imap.gmail.com";
        String port = "993";
        String username = eMail;
        String password = appPassword;
        String expectedSubject="amazon.in: Password recovery";
        String expectedBodyContent = "Someone is attempting to reset the password of your account.";
        String expctedEmailfrom= "\"amazon.in\" <account-update@amazon.in>";
        String url = null;
        try {
            // Configure JavaMail properties and establish a secure IMAP session with Gmail
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.host", "host");
            properties.put("mail.imap.port", "port");
            properties.put("mail.imap.ssl.enable", "true");
            //connect to the mail server
            Session emailSession;
            emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);
            // Open Inbox folder and retrieve all unread emails
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            //search for unread emails
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            boolean found = false;
            // Iterate through unread emails and locate the latest Amazon password recovery email
            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];
                String subject = message.getSubject();
                if (subject.contains(expectedSubject)) {
                    found = true;
                    System.out.println("Subject: " + message.getSubject());
                    Assert.assertEquals(message.getSubject(), expectedSubject);
                    System.out.println("expectedEmailFrom: " + message.getFrom()[0]);
                   Assert.assertEquals(message.getFrom()[0].toString(), expctedEmailfrom);
                    String actualEmailBody=getTextFromMesssage(message);
                    Assert.assertTrue(actualEmailBody.contains(expectedBodyContent));
 //                  System.out.println("Email Body: " + getTextFromMesssage(message));
                    // Read the email body and ex7tract the password recovery URL from the email content
                    url = extractUrl(actualEmailBody);
                    System.out.println("Extracted URL: " + url);
                    break;
                }
            }
            if (!found) {
                System.out.println("Email with subject 'amazon.in: Password recovery' is not received.");
            }
            // Close mailbox resources after email processing is complete
            inbox.close(false);
            store.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Navigate to the extracted URL and verify that the expected password recovery page is displayed
    driver.navigate().to(url);
        Assert.assertTrue(driver.findElement(By.name("customerResponseDenyButton")).isDisplayed() && driver.findElement(By.name("customerResponseDenyButton")).isEnabled());
        driver.quit();
    }

    // Extracts the first HTTPS URL present in the email content using Regular Expression
    private static String extractUrl(String emailBody) {
        Pattern pattern = Pattern.compile("https://[^\s\"')]+");
        Matcher matcher = pattern.matcher(emailBody);

        String url = null;
        if (matcher.find()) {
            url = matcher.group();
        }

        return url;
    }

    // Reads email content and returns it as plain text regardless of whether the email is text, HTML, or multipart
    private static String getTextFromMesssage(Message message) throws Exception {
        String result = " ";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        } else if (message.isMimeType("text/html")) {
            result = message.getContent().toString();
        }
        return result;
    }

    // Recursively parses multipart email sections and combines their textual content into a single string
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                result = result + "\n" + bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result.toString();
    }

}

