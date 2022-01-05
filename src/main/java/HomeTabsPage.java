import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;

public class HomeTabsPage extends PageBase {

    public HomeTabsPage(AndroidDriver<MobileElement> androidDriver) {
        super(androidDriver);
    }

    //Der "Zustimmen"-Button im Consent-Overlay beim Start der App
    @FindBy(xpath = "//div[@id=\"notice\"]/div[4]/div[1]/button")
    MobileElement consentAcceptBtn;

    //Hamburgermenü
    @AndroidFindBy(accessibility = "Navigate Up")
    MobileElement hamburgerMenuBtn;

    //Suche-Button
    @AndroidFindBy(accessibility = "Suche")
    MobileElement searchBtn;

    public void clickConsentAcceptBtn(){
        click(consentAcceptBtn);
    }

    public void clickHamburgerMenuBtn(){
        click(hamburgerMenuBtn);
    }

    public void clickSearchBtn(){
        click(searchBtn);
    }


}