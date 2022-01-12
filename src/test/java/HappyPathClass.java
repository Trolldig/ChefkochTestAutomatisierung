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
import java.util.concurrent.TimeUnit;


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
        }
    }

    /**
     * Performs swipe from the center of screen or element
     * Die Methode habe ich größenteils nicht selbst gecoded, sondern nur so angepasst, dass man einen eignen Startpunkt
     * angeben kann. Wäre es vielleicht besser, dafür zwei getrennte Methoden zu nutzen? Eine Methode, welche die Swipe-
     * Geste immer an den gleichen Koordinaten ausführt. Und eine, welche für bestimmte Elemente wie Slider benutzt wird?
     *
     * @param xStart x-coordinate of element
     * @param yStart y-coordinate of element
     * @param dir the direction of swipe
     * @param useCoordinates If true, the method uses the start coordinates. If false, it uses the center of the screen
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
                pointEnd = new Point(dims.width - edgeBorder, yCoordinate);
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

    /**
     * Scrollt in den Suchergebnissen nach unten und überprüft ob ein gesuchter Begriff in den neu dargestellten
     * Rezepten gefunden wird.
     * @param recipeName Gesuchter Teilbegriff eines Rezepttitels
     * @return Ob der Teilbegriff in den dargestellten Rezepten gefunden wurde
     */
    public boolean swipeAndCheckForRecipe(String recipeName){
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
        // Spricht etwas dagegen, das Setup über den ersten Test aufzurufen, anstatt die @BeforeTest Annotation zu
        // verwenden?
        setup();
        // Auf meinem langsamerem Laptop schlug der Test manchmal fehl, weil die App zu langsam geladen hat
        // Die while-schleife wartet darauf, dass neben dem nativen view noch der webview mit dem consent-banner
        // geladen wude. Ich bin mir nicht sicher, ob die Art zu "warten" so klug ist. Theoretisch kann das natürlich
        // zu in einer unendlichen Schleife führen
        while(driver.getContextHandles().size() <= 1){
            Thread.sleep(1000);
        }
        changeContext();
        homeTabsPage = new HomeTabsPage(driver);
        homeTabsPage.clickConsentAcceptBtn();
        changeContext();
    }

    // Sucht mithilfer einer json-datei 2x nach einem Rezept.
    @Test(dataProvider = "search data", priority = 2)
    void theCakeIsALie(String searchTerm1, String searchterm2, String searchSuggestion2, String recipeTitle) throws InterruptedException {
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
        setBackToHomeActivity();
    }

    //Wechselt den Tab auf der HomeActivity mit einem Swipe
    @Test (priority = 3)
    void swipeHomeTabSlider() throws InterruptedException{
        int [] coordinates = homeTabsPage.getBannerPosition();
        swipeScreenWith(coordinates[0], coordinates[1],"LEFT",true);
        coordinates = homeTabsPage.getBannerPosition();
        swipeScreenWith(coordinates[0], coordinates[1],"RIGHT",true);
    }

    //Swiped durch den Content-Slider in der HomeActivity
    @Test (priority = 4)
    void swipeHomeAktuellesSlider() throws InterruptedException{
        int [] coordinates = homeTabsPage.getAktuellesSliderPosition();
        swipeScreenWith(coordinates[0], coordinates[1],"LEFT",true);
        swipeScreenWith(coordinates[0], coordinates[1],"RIGHT",true);
        Thread.sleep(5000);
    }

}