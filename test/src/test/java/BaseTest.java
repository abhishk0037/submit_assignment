import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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

    private static final String USERNAME = "abhishekydv9099";
    private static final String ACCESS_KEY = "suKmJKnxMzXZ5a0AqZSzc85Dow8lYUKw9nw4fUYJPxMvB1hUXu";
    private static final String GRID_URL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    @Parameters({"browser", "version", "platform", "executionMode"})
    public void setup(String browser, String version, String platform, String executionMode) throws MalformedURLException {
        if (executionMode.equalsIgnoreCase("local")) {
            if (browser.equalsIgnoreCase("chrome")) {
                driver.set(new ChromeDriver());
            } else if (browser.equalsIgnoreCase("firefox")) {
                driver.set(new FirefoxDriver());
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            driver.get().manage().window().maximize();
        } else if (executionMode.equalsIgnoreCase("lambdatest")) {
            DesiredCapabilities capabilities = getDesiredCapabilities(browser, version, platform);

            driver.set(new RemoteWebDriver(new URL("https://" + USERNAME + ":" + ACCESS_KEY + GRID_URL), capabilities));
        }
        else {
            throw new IllegalArgumentException("Invalid execution mode: " + executionMode);
        }
    }

    private DesiredCapabilities getDesiredCapabilities(String browser, String version, String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", version);

        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", platform);
        ltOptions.put("project", "AmazonAutomation");
        ltOptions.put("build", "TestNG Parallel Run");
        ltOptions.put("name", "SearchProductTest");

        capabilities.setCapability("LT:Options", ltOptions);
        return capabilities;
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
