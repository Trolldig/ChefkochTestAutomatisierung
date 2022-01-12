import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.ListIterator;

public class HomeTabsPage extends PageBase {

    public HomeTabsPage(AndroidDriver<MobileElement> androidDriver) {
        super(androidDriver);
    }

    //Der "Zustimmen"-Button im Consent-Overlay beim Start der App
    //xpath sollte man vermeiden, aber einen anderen Weg habe ich für den Webview-Button nicht gefunden
    @FindBy(xpath = "//div[@id=\"notice\"]/div[4]/div[1]/button")
    MobileElement consentBtn;

    //Hamburgermenü
    @AndroidFindBy(accessibility = "Navigate up")
    MobileElement hamburgerMenuBtn;

    //Suche-Button
    @AndroidFindBy(accessibility = "Suche")
    MobileElement searchBtn;

    //Startseite-Button im Hamburgermenü
    @AndroidFindBy(id = "de.pixelhouse:id/nav_home")
    MobileElement navHomeBtn;

    //Startseite-Button im Hamburgermenü
    @AndroidFindBy(id = "de.pixelhouse:id/nav_search")
    MobileElement navSearchBtn;

    //Werbebanner, kann zum Swipen der Tabs benutzt werden
    @FindBy(id = "de.pixelhouse:id/ad_banner_root")
    MobileElement banner;

    //Slider auf der Startseite
    @FindBy (id = "de.pixelhouse:id/main_slider_compose_view")
    MobileElement aktuellesSlider;

    /**
     * Klickt den Zustimmen-Button im Consent-Overlay
     */
    public void clickConsentAcceptBtn(){
        click(consentBtn);
    }

    public void clickHamburgerMenuBtn(){
        click(hamburgerMenuBtn);
    }

    public void clickSearchBtn(){
        click(searchBtn);
    }

    public void clickNavSearchBtn(){
        click(navSearchBtn);
    }

    /**
     * Gibt die Koordinaten des Werbebanners auf der Startseite zurück
     * @return center coordinates of the element
     */
    public int[] getBannerPosition(){
        return getElementPosition(banner);
    }

    /**
     * Gibt die Koordinaten des Sliders "aktuelles" auf der Startseite zurück
     * @return center coordinates of the element
     */
    public int[] getAktuellesSliderPosition(){
        return getElementPosition(aktuellesSlider);
    }

}