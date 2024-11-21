package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    public WebElement findElement(By by, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void clickElement(By by, WebDriver driver) {
        findElement(by, driver).click();
    }

    public void scrollToRight(By by, WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = findElement(by, driver);
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", element);
    }

    public void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)");
    }

    public void search(By by, String text, WebDriver driver) {
        WebElement searchBox = findElement(by, driver);
        searchBox.clear();
        searchBox.sendKeys(text);
        searchBox.submit();
    }
}
