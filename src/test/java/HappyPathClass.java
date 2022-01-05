import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.annotations.Test;
import java.util.Set;


public class HappyPathClass extends BaseClass{

    HomeTabsPage homeTabsPage;

    public void printContextNames(){
        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
            System.out.println(contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
        }
        driver.context("WEBVIEW_de.pixelhouse");
    }

    @Test
    void theCakeIsALie() throws InterruptedException {

        //driver.openNotifications();
        //driver.navigate().back();
        Thread.sleep(10000);
        printContextNames();
        //driver.findElementByXPath("//div[@id=\"notice\"]/div[4]/div[1]/button").click();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickConsentAcceptBtn();
        driver.context("NATIVE_APP");
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickSearchBtn();
        homeTabsPage.clickSearchTextField();
        homeTabsPage.sendTextSearchBar("Apfelkuchen");
        homeTabsPage.clickClearBtn();
        homeTabsPage.sendTextSearchBar("Schokoladenkuchen");
        ((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));


        //homeTabsPageMobile = new HomeTabsPageMobile(driver);
        //driver.findElementById("00000000-0000-01be-0000-00310000020e").click();
        //WebElement notice = driver.findElementById("notice");
        //WebElement acceptBtn = notice.findElement(By.partialLinkText("Zustimmen"));
        //acceptBtn.click();
        //WebElement acceptButton = ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator("new UiSelector().textContains(\"Zustimmen\")");
        //acceptButton.click();
        //Thread.sleep(5000);
        //driver.findElementByAccessibilityId("Suche").click();
        //MobileElement el1 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View[2]/android.widget.Button");
        //el1.click();
        Thread.sleep(5000);
    }


}