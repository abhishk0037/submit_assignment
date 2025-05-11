import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static final String USERNAME = "abhishekydv9099";
    public static final String ACCESS_KEY = "suKmJKnxMzXZ5a0AqZSzc85Dow8lYUKw9nw4fUYJPxMvB1hUXu";
    public static final String GRID_URL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    @Parameters({"browser", "version", "platform"})
    public void setup(String browser, String version, String platform) throws MalformedURLException {
//        driver.set(new ChromeDriver());
//        driver.get().manage().window().maximize();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", version);

        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", platform);
        ltOptions.put("project", "AmazonAutomation");
        ltOptions.put("build", "TestNG Parallel Run");
        ltOptions.put("name", "SearchProductTest");

        capabilities.setCapability("LT:Options", ltOptions);

        driver.set(new RemoteWebDriver(new URL("https://" + USERNAME + ":" + ACCESS_KEY + GRID_URL), capabilities));
    }

    @AfterMethod
    public void tearDown() {
        driver.get().quit();
        driver.remove();
    }

    public WebDriver getDriver() {
        return driver.get();
    }
}
