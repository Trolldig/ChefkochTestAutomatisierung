import io.appium.java_client.TouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilits.JsonReader;

import java.io.IOException;
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
    public void swipeScreenWith(int xStart, int yStart, String dir, boolean useCoordinates) {
        System.out.println("swipeScreen(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 200; // ms

        int edgeBorder = 10; // better avoid edges
        int xCoordinate;
        int yCoordinate;
        Point pointStart, pointEnd;
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Dimension dims = driver.manage().window().getSize();

        if(useCoordinates){
            xCoordinate = xStart;
            yCoordinate = yStart;
            pointStart = new Point(xCoordinate, yCoordinate);
        } else {
            xCoordinate = dims.width;
            yCoordinate = dims.height;
            pointStart = new Point(xCoordinate / 2, yCoordinate / 2);
        }
        // init start point = given coordinates or center of screen if irrelevant
        //pointStart = new Point(xCoordinate / 2, yCoordinate / 2);

        switch (dir) {
            case "DOWN": // center of footer
                pointEnd = new Point(xCoordinate / 2, yCoordinate - (yCoordinate / 3) - edgeBorder);
                break;
            case "UP": // center of header
                pointEnd = new Point(xCoordinate / 2, xCoordinate / 3 + edgeBorder);
                break;
            case "LEFT": // center of left side
                pointEnd = new Point(edgeBorder, yCoordinate);
                break;
            case "RIGHT": // center of right side
                pointEnd = new Point(xCoordinate - edgeBorder, yCoordinate);
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
        swipeScreenWith(0,0,"UP",false);
        return searchPage.recipeTitlesContain(recipeName);
    }

    /**
     * Navigiert durch back-button zurück, bis der der Home-Screen zu sehen ist
     * @throws InterruptedException
     */
    public void setBackToHomeActivity() throws InterruptedException {
        String homeActivity = ".chefkoch.app.screen.hometabs.HomeTabsActivity";
        setBackToActivity(homeActivity);
    }

    @DataProvider(name = "search data")
    public Object [][] passData() throws IOException, ParseException {
        return JsonReader.getJSONData(System.getProperty("user.dir")+"/data/SearchData.json", "Search Data", 4);
    }

    @Test (priority = 1)
    void acceptConsent() throws InterruptedException{
        setup();
        while(driver.getContextHandles().size() <= 1){
            Thread.sleep(1000);
        }
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickConsentAcceptBtn();
        changeContext();
    }

    @Test(dataProvider = "search data", priority = 2)
    void theCakeIsALie(String searchTerm1, String searchterm2, String searchSuggestion2, String recipeTitle) throws InterruptedException {
        //driver.openNotifications();
        //driver.navigate().back();
        //Thread.sleep(10000);
        //changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickSearchBtn();
        System.out.println(driver.currentActivity());
        searchStartPage = new SearchStartPage(driver);
        searchStartPage.clickSearchTextField();
        searchStartPage.sendTextSearchBar(searchTerm1);
        searchStartPage.clickClearBtn();
        searchStartPage.sendTextSearchBar(searchterm2);
        searchStartPage.clickSuggestion(searchSuggestion2);
        System.out.println(driver.currentActivity());
        searchPage = new SearchPage(driver);
        searchPage.clickFilterBtn();
        searchPage.clickFilterFourStarsBtn();
        searchPage.clickFilterApplyBtn();
        searchPage.recipeTitlesContain(recipeTitle);
        while(!recipeFound && counter < 5){
            if(swipeAndCheckForRecipe(recipeTitle)){
                recipeFound = true;
            }
            counter++;
        }
        if(recipeFound){
            searchPage.clickRecipe(recipeTitle);
            recipeFound = false;
        } else {
            searchPage.clickRandomRecipe();
        }

        //searchPage.clickRecipe("Saftiger");
        //searchPage.sendTextSearchBar("h");
        //searchPage.clickRandomSuggestion();

        //driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        //Thread.sleep(5000);
        setBackToHomeActivity();
    }

    @Test (priority = 3)
    void swipeHomeSlider() throws InterruptedException{
        int [] coordinates = homeTabsPage.getBannerPosition();
        Thread.sleep(1000);
        swipeScreenWith(coordinates[0], coordinates[1],"LEFT",true);

        Thread.sleep(5000);
    }

}