import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.annotations.Test;
import java.util.Set;


public class HappyPathClass extends BaseClass{

    HomeTabsPage homeTabsPage;
    SearchStartPage searchStartPage;
    SearchPage searchPage;

    /**
     * Wechselt zwischen Webview und nativer App, falls mehr als ein Context vorhanden ist
     */
    public void changeContext(){
        Set<String> contextNames = driver.getContextHandles();
        if(contextNames.size()>1){
            if (driver.getContext().equals(contextNames.toArray()[1])){
                driver.context((String) contextNames.toArray()[0]);
                System.out.println(driver.getContext());
            } else {
                driver.context((String) contextNames.toArray()[1]);
                System.out.println(driver.getContext());
            }
        } else {
            driver.context((String) contextNames.toArray()[0]);
            System.out.println("Contexts: "+contextNames.size());
        }
    }

    @Test
    void acceptConsent() throws InterruptedException{
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickConsentAcceptBtn();
    }

    @Test
    void theCakeIsALie() throws InterruptedException {

        //driver.openNotifications();
        //driver.navigate().back();
        //Thread.sleep(10000);
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickSearchBtn();
        System.out.println(driver.currentActivity());
        searchStartPage = new SearchStartPage(driver);
        searchStartPage.clickSearchTextField();
        searchStartPage.sendTextSearchBar("Apfelkuchen");
        searchStartPage.clickClearBtn();
        searchStartPage.sendTextSearchBar("Schokola");
        searchStartPage.clickSuggestion("Schokoladenkuchen");
        System.out.println(driver.currentActivity());
        searchPage = new SearchPage(driver);
        searchPage.clickRecipe("Saftiger");
        //searchPage.sendTextSearchBar("h");
        //searchPage.clickRandomSuggestion();

        //driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        Thread.sleep(5000);
    }


}