import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;

public class PageBase {

    private AndroidDriver<MobileElement> driver;
    public static final long WAIT = 10;

    public PageBase(AndroidDriver<MobileElement> androidDriver) {
        PageFactory.initElements(new AppiumFieldDecorator(androidDriver), this);
        //PageFactory.initElements(androidDriver, this);
        this.driver = androidDriver;
    }

    public void waitForVisibility(MobileElement element){
        WebDriverWait wait = new WebDriverWait(driver, WAIT);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void clear(MobileElement element) {
        waitForVisibility(element);
        element.clear();
    }

    public void click(MobileElement element) {
        waitForVisibility(element);
        element.click();
    }


    public void sendText(MobileElement element, String text) {
        waitForVisibility(element);
        element.sendKeys(text);
    }

    public String getAttribute(MobileElement element, String attribute) {
        waitForVisibility(element);
        return element.getAttribute(attribute);
    }

    public Boolean findElementByID(String id){
        try {
            driver.findElementById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}