import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

public class SearchProduct extends BaseTest {

    @DataProvider(name = "productData", parallel = true)
    public Object[][] getData() {
        return new Object[][]{
                {"iphone 16", "Works with AirPods; Black"},
                {"galaxy s25", "50MP Camera with Galaxy AI"}
        };
    }

    @Test(dataProvider = "productData")
    public void searchAndAddToCart(String productName, String partialLinkText) throws InterruptedException {
        getDriver().get("https://www.amazon.in/");
        Thread.sleep(20000);

        getDriver().findElement(By.xpath("//div[@class='nav-search-field ']//input")).sendKeys(productName);
        getDriver().findElement(By.id("nav-search-submit-button")).click();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(partialLinkText))).click();

        // Switch to the new tab
        Set<String> handles = getDriver().getWindowHandles();
        ArrayList<String> tabs = new ArrayList<>(handles);
        getDriver().switchTo().window(tabs.get(1));

        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='a-button-stack']//input[@id='add-to-cart-button' and @type='submit']")));
        addToCartButton.click();


        WebElement subtotal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='attach-accessory-cart-subtotal']")));

        String text = subtotal.getText();
        String amount = text.split("\\.")[0].trim();
        System.out.println("Price of the product '" + productName + "' is: " + amount);
    }
}



