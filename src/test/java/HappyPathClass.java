import org.testng.annotations.Test;
import java.util.Set;


public class HappyPathClass extends BaseClass{

    HomeTabsPage homeTabsPage;
    SearchPage searchPage;

    /**
     * Wechselt zwischen Webview und nativer App
     */
    public void changeContext(){
        Set<String> contextNames = driver.getContextHandles();
        if (driver.getContext().equals(contextNames.toArray()[1])){
            driver.context((String) contextNames.toArray()[0]);
            System.out.println(driver.getContext());
        } else {
            driver.context((String) contextNames.toArray()[1]);
            System.out.println(driver.getContext());
        }
    }

    @Test
    void theCakeIsALie() throws InterruptedException {

        //driver.openNotifications();
        //driver.navigate().back();
        Thread.sleep(10000);
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickConsentAcceptBtn();
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickSearchBtn();
        searchPage = new SearchPage(driver);
        searchPage.clickSearchTextField();
        searchPage.sendTextSearchBar("Apfelkuchen");
        searchPage.clickClearBtn();
        searchPage.sendTextSearchBar("Schokola");
        searchPage.clickSuggestion("Schokoladenkuchen");
        //searchPage.sendTextSearchBar("h");
        //searchPage.clickRandomSuggestion();

        //driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        Thread.sleep(5000);
    }


}