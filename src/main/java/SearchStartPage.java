import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class SearchStartPage extends PageBase{

    public SearchStartPage(AndroidDriver<MobileElement> androidDriver) {
        super(androidDriver);
    }

    //Hamburgermenü
    @AndroidFindBy(accessibility = "Navigate up")
    private MobileElement hamburgerMenuBtn;

    //Suchleiste
    @FindBy(id = "de.pixelhouse:id/search_bar_text")
    private MobileElement searchTextField;

    //Löschen-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/clear_btn")
    private MobileElement searchClearBtn;

    //Suche-Button
    @AndroidFindBy(accessibility = "Suche")
    private MobileElement searchBtn;

    //Container mit Liste der Suchvorschläge
    @AndroidFindBy(id = "de.pixelhouse:id/suggestions_list")
    private MobileElement suggestionContainer;

    public void clickHamburgerMenuBtn(){
        click(hamburgerMenuBtn);
    }

    public void clickSearchTextField(){
        click(searchTextField);
    }

    public void clickClearBtn(){
        click(searchClearBtn);
    }

    public void sendTextSearchBar(String textInput){
        if(textInput != null) sendText(searchTextField, textInput);
    }

    public void clickSearchBtn(){
        click(searchBtn);
    }

    /**
     *  Checkt, ob Suchvorschläge vorhanden sind
     * @return true wenn Suchvorschläge vohanden sind, false sonst
     */
    public Boolean suggestionContainerFound(){
        Boolean suggestionContainerFound = findElementByID("de.pixelhouse:id/suggestions_list");
        return suggestionContainerFound;
    }

    /**
     * Prüft, ob ein bestimmtes Wort in den Suchvorschlägen vorkommt.
     * @param searchedText der gesuchte Begriff
     * @return true wenn das Wort in den Suggestions vorkommt, false sonst
     */
    public boolean suggestionListContainsWord(String searchedText){
        if (suggestionContainerFound()) {
            Boolean textFound = false;
            // Packt alle TextViewsElemente der Vorschläge in eine Liste
            List<MobileElement> suggestions = suggestionContainer.findElementsByClassName("android.widget.TextView");
            // Geht alle Suchvorschläge durch und vergleicht sie mit dem Suchbegriff
            // Suchbegriffe werden immer Kleingeschrieben
            for (MobileElement textElement : suggestions){
                if (textElement.getText().equals(searchedText.toLowerCase())){
                    textFound = true;
                    System.out.println("Liste enthält: " + searchedText);
                }
            }
            return textFound;
        } else {
            System.out.println("Keine Vorschläge vorhanden");
            return false;
        }
    }

    /**
     * Klickt einen bestimmten Suchbegriff in den Vorschlägen an, falls vorhanden
     * @param searchedText Suchbegriff der in den Vorschlägen geklickt werden soll
     */
    public void clickSuggestion(String searchedText){
        if (suggestionListContainsWord(searchedText)){
            Boolean suggestionFound = false;
            List<MobileElement> suggestionList = suggestionContainer.findElementsByClassName("android.widget.TextView");
            ListIterator<MobileElement> suggestionListIterator = suggestionList.listIterator();
            while(!suggestionFound){
                if (suggestionListIterator.next().getText().equals(searchedText.toLowerCase())){
                    suggestionFound = true;
                    suggestionListIterator.previous();
                    click(suggestionListIterator.next());
                }
            }
        }
    }

    /**
     * Klickt einen zufälligen vorgeschlagenen Suchbegriff an
     */
    public void clickRandomSuggestion(){
        if (suggestionContainerFound()) {
            Boolean textFound = false;
            // Packt alle TextViewsElemente der Vorschläge in eine Liste
            List<MobileElement> suggestionList = suggestionContainer.findElementsByClassName("android.widget.TextView");
            Random rand = new Random();
            // Wält einen zufälligen Begriff aus
            click(suggestionList.get(rand.nextInt(suggestionList.size())));
        } else {
            System.out.println("Keine Vorschläge vorhanden");
        }
    }
}
