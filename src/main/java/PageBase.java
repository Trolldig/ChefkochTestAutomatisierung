import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Point;
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
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception exp){
            System.out.println("Grund: " + exp.getCause());
            System.out.println("Fehlermeldung: " + exp.getMessage());
            exp.printStackTrace();
        }

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
        } catch (Exception exp){
            return false;
        }
    }

    public int[] getElementPosition(MobileElement element){
        Point a = element.getLocation();
        int [] coordinates = new int [2];
        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        coordinates[0] = (rightX + leftX) / 2;
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        coordinates[1] = (upperY + lowerY) / 2;

        return coordinates;
    }
}