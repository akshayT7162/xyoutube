package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;

        Wrappers wrapper = new Wrappers();
        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */

        @Test
        public void testCase01() {
                driver.get("https://www.youtube.com/");
                WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                menuButton.click();
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(driver.getCurrentUrl().contains("youtube.com"),
                                "URL does not contain 'youtube.com'");

                wrapper.clickElement(By.linkText("About"), driver);
                WebElement aboutText = wrapper.findElement(By.tagName("body"), driver);
                System.out.println("About text: " + aboutText.getText());
                softAssert.assertAll();
        }

        @Test
        public void testCase02() {
                driver.get("https://www.youtube.com/");
                WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                menuButton.click();
                wrapper.clickElement(By.xpath("//a[@title='Movies']"), driver);
                wrapper.scrollToRight(By.xpath(
                                "//span[@id='title' and contains(text(),'Top selling')]/ancestor::div[@id='dismissible']//button[@aria-label='Next']"),
                                driver);

                List<WebElement> movies = driver.findElements(By.cssSelector(".movie-element"));
                SoftAssert softAssert = new SoftAssert();
                for (WebElement movie : movies) {
                        String rating = movie.findElement(By.cssSelector(".rating")).getText();
                        softAssert.assertTrue(rating.contains("A") || rating.contains("PG"),
                                        "Rating is not 'A' or 'PG'");
                        String category = movie.findElement(By.cssSelector(".category")).getText();
                        softAssert.assertTrue(category.equals("Comedy") || category.equals("Animation")
                                        || category.equals("Drama"), "Category does not match");
                }
                softAssert.assertAll();
        }

        @Test
        public void testCase03() {
                driver.get("https://www.youtube.com/");
                WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                menuButton.click();
                wrapper.clickElement(By.xpath("//a[@title='Music']"), driver);
                wrapper.scrollToRight(By.xpath(
                                "//span[@id='title' and contains(text(),'Biggest Hits')]/ancestor::div[@id='dismissible']//button[@aria-label='Next']"),
                                driver);

                List<WebElement> playlists = driver.findElements(By.cssSelector(".playlist-section .playlist"));
                SoftAssert softAssert = new SoftAssert();
                for (WebElement playlist : playlists) {
                        String name = playlist.findElement(By.cssSelector(".playlist-name")).getText();
                        System.out.println("Playlist name: " + name);
                        int trackCount = Integer
                                        .parseInt(playlist.findElement(By.cssSelector(".track-count")).getText());
                        softAssert.assertTrue(trackCount <= 50, "Track count exceeds 50");
                }
                softAssert.assertAll();
        }

        @Test
        public void testCase04() {
                driver.get("https://www.youtube.com/");
                WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                menuButton.click();
                wrapper.clickElement(By.linkText("News"), driver);

                List<WebElement> newsPosts = driver.findElements(By.xpath("//span[@id='title' and contains(text(),'Latest news posts')]//ancestor::div[@id='dismissible']"));
                int postsToProcess = Math.min(newsPosts.size(), 3); // Use the smaller of 3 or the list size
                int totalLikes = 0;

                for (int i = 0; i < postsToProcess; i++) {
                        WebElement post = newsPosts.get(i);
                        String title = post.findElement(By.cssSelector(".title")).getText();
                        String body = post.findElement(By.cssSelector(".body")).getText();
                        System.out.println("Title: " + title);
                        System.out.println("Body: " + body);

                        int likes = Integer.parseInt(
                                        post.findElement(By.cssSelector(".likes")).getText().replace("K", "000"));
                        totalLikes += likes;
                }
                System.out.println("Total likes: " + totalLikes);
        }
        
        @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
                public void testCase05(String searchItem) throws Exception {
                    // Navigate to YouTube
                    driver.get("https://www.youtube.com/");
                
                    // Locate the search box and search button
                    WebElement searchbox = driver.findElement(By.xpath("//input[@id='search']"));
                    WebElement searchButton = driver.findElement(By.xpath("//button[@id='search-icon-legacy']"));
                
                    // Clear the search box, enter the search item, and click the search button
                    searchbox.clear();
                    searchbox.sendKeys(searchItem);
                    searchButton.click();
                
                    // Log the search term
                    System.out.println("Searched for: " + searchItem);
                
                    // Optionally validate the search results
                    WebElement resultsContainer = new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.presenceOfElementLocated(By.id("contents")));
                    SoftAssert softAssert = new SoftAssert();
                    softAssert.assertTrue(resultsContainer.isDisplayed(), "Search results are not displayed for: " + searchItem);
                    softAssert.assertAll();
                }
        

        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}