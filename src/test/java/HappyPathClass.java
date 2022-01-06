import io.appium.java_client.TouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;


public class HappyPathClass extends BaseClass{

    private int counter = 0;
    private boolean recipeFound = false;

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

    /**
     * Performs swipe from the center of screen
     *
     * @param dir the direction of swipe
     **/
    public void swipeScreenWith(String dir) {
        System.out.println("swipeScreen(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 200; // ms

        int edgeBorder = 10; // better avoid edges
        Point pointStart, pointEnd;
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Dimension dims = driver.manage().window().getSize();

        // init start point = center of screen
        pointStart = new Point(dims.width / 2, dims.height / 2);

        switch (dir) {
            case "DOWN": // center of footer
                pointEnd = new Point(dims.width / 2, dims.height - (dims.height / 3) - edgeBorder);
                break;
            case "UP": // center of header
                pointEnd = new Point(dims.width / 2, dims.width / 3 + edgeBorder);
                break;
            case "LEFT": // center of left side
                pointEnd = new Point(edgeBorder, dims.height / 2);
                break;
            case "RIGHT": // center of right side
                pointEnd = new Point(dims.width - edgeBorder, dims.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeScreen(): dir: '" + dir.toString() + "' NOT supported");
        }

        // execute swipe using TouchAction
        pointOptionStart = PointOption.point(pointStart.x, pointStart.y);
        pointOptionEnd = PointOption.point(pointEnd.x, pointEnd.y);
        System.out.println("swipeScreen(): pointStart: {" + pointStart.x + "," + pointStart.y + "}");
        System.out.println("swipeScreen(): pointEnd: {" + pointEnd.x + "," + pointEnd.y + "}");
        System.out.println("swipeScreen(): screenSize: {" + dims.width + "," + dims.height + "}");
        try {
            new TouchAction(driver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeScreen(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public boolean swipeAndCheckForRecipe(String recipeName){
        searchPage.recipeTitlesContain(recipeName);
        swipeScreenWith("UP");
        return searchPage.recipeTitlesContain(recipeName);
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
        searchPage.clickFilterBtn();
        searchPage.clickFilterFourStarsBtn();
        searchPage.clickFilterApplyBtn();
        searchPage.recipeTitlesContain("tarte");
        while(!recipeFound && counter < 5){
            if(swipeAndCheckForRecipe("tarte")){
                recipeFound = true;
            }
            counter++;
        }
        if(recipeFound){
            searchPage.clickRecipe("tarte");
        } else {
            searchPage.clickRandomRecipe();
        }

        //searchPage.clickRecipe("Saftiger");
        //searchPage.sendTextSearchBar("h");
        //searchPage.clickRandomSuggestion();

        //driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        Thread.sleep(5000);
    }


}