import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomeTabsPage extends PageBase {

    public HomeTabsPage(AndroidDriver<MobileElement> androidDriver) {
        super(androidDriver);
    }

    //Hamburgermenü
    @AndroidFindBy(accessibility = "Navigate Up")
    MobileElement hamburgerMenuBtn;

    //Der "Zustimmen"-Button im Consent-Overlay beim Start der App
    @FindBy(xpath = "//div[@id=\"notice\"]/div[4]/div[1]/button")
    MobileElement consentAcceptBtn;

    //Suche Button
    @AndroidFindBy(accessibility = "Suche")
    //@FindBy(id = "de.pixelhouse:id/action_search")
    MobileElement searchBtn;

    //Suchleiste
    @FindBy(id = "de.pixelhouse:id/search_bar_text")
    MobileElement searchTextField;

    //Löschen-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/clear_btn")
    MobileElement searchClearBtn;

    public void clickHamburgerMenuBtn(){
        click(hamburgerMenuBtn);
    }

    public void clickConsentAcceptBtn(){
        click(consentAcceptBtn);
    }

    public void clickSearchBtn(){
        click(searchBtn);
    }

    public void clickSearchTextField(){
        click(searchTextField);
    }

    public void clickClearBtn(){
        click(searchClearBtn);
    }

    public void sendTextSearchBar(String textInput){
        sendText(searchTextField, textInput);
    }
}