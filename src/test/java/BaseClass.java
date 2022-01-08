import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;


public class BaseClass {

    public AndroidDriver<MobileElement> driver;

    //@BeforeTest
    void setup(){

        try {
            DesiredCapabilities caps = new DesiredCapabilities();

            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
            caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11");
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
            //caps.setCapability(MobileCapabilityType.UDID, "");
            caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
            caps.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+ "/apps/Chefkoch Rezepte Kochen_v6.2.3_apkpure.com.apk");
            caps.setCapability("appPackage", "de.pixelhouse");
            caps.setCapability("appActivity", ".chefkoch.app.screen.hometabs.HomeTabsActivity");
            caps.setCapability("appWaitPackage", "de.pixelhouse");
            caps.setCapability("appWaitActivity", ".chefkoch.app.screen.hometabs.HomeTabsActivity");
            caps.setCapability("appWaitDuration", "50000");
            caps.setCapability("chromedriverExecutable", System.getProperty("user.dir")+ "/drivers/chromedriver.exe");

            URL url = new URL("http://localhost:4723/wd/hub");

            driver = new AndroidDriver(url,caps);
        }catch(Exception exp){
            System.out.println("Grund: " + exp.getCause());
            System.out.println("Fehlermeldung: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * Navigiert mit Back-Button zurück, bis eine bestimmte Activity geöffnet ist
     * @param goalActivity
     * @throws InterruptedException
     */
    public void setBackToActivity(String goalActivity) throws InterruptedException {
        String currentActivity = driver.currentActivity();
        if(!currentActivity.equals(goalActivity)){
            while(!currentActivity.equals(goalActivity)){
                driver.pressKey(new KeyEvent(AndroidKey.BACK));
                currentActivity = driver.currentActivity();
            }
        }
    }

    //@Test
    void sampleTest() throws InterruptedException {
        System.out.println("sampleTest ist fertig. (Funktionslos)");
    }

    @AfterTest
    void tearDown(){
        if (null != driver) {
            if (driver.isKeyboardShown()){
                driver.hideKeyboard();
            }
            driver.quit();
        }
    }
}