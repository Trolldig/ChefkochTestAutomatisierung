import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class SearchPage extends PageBase{

    public SearchPage(AndroidDriver<MobileElement> androidDriver) {
        super(androidDriver);
    }

    //Hamburgermenü
    @AndroidFindBy(accessibility = "Navigate up")
    MobileElement navigateBackBtn;

    //Back-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/left_action")
    MobileElement searchBackBtn;

    //Suchleiste
    @FindBy(id = "de.pixelhouse:id/search_bar_text")
    MobileElement searchTextField;

    //Kebap Menü
    @FindBy(id = "de.pixelhouse:id/search_bar_overflow_menu")
    MobileElement kebapMenu;

    //Löschen-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/clear_btn")
    MobileElement searchClearBtn;

    //Suche-Button
    @AndroidFindBy(accessibility = "Suche")
    MobileElement searchBtn;

    //Container mit Liste der Suchvorschläge
    @AndroidFindBy(id = "de.pixelhouse:id/suggestions_list")
    MobileElement suggestionContainer;

    //Filter-Btn
    @FindBy(id = "de.pixelhouse:id/search_filter_text_tv")
    MobileElement filterBtn;

    //4Sterne & mehr Filter-Btn
    @FindBy(id = "de.pixelhouse:id/mr_four_stars")
    MobileElement filterFourStarsBtn;

    //Rezeptliste
    @FindBy(id = "de.pixelhouse:id/list")
    MobileElement recipeListContainer;

    public void clickNavigateBackBtn(){
        click(navigateBackBtn);
    }

    public void clickSearchBackBtn(){
        click(searchBackBtn);
    }

    public void clickSearchTextField(){
        click(searchTextField);
    }

    public void clickKebapMenu(){
        click(kebapMenu);
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

    public void clickFilterBtn(){
        click(filterBtn);
    }

    public void clickFilterFourStarsBtn(){
        click(filterFourStarsBtn);
    }

    /**
     *  Checkt, ob Suchvorschläge vorhanden sind
     * @return true wenn Suchvorschläge vohanden sind, false sonst
     */
    private Boolean suggestionContainerFound(){
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
                String suggestion = textElement.getText();
                if (suggestion.equals(searchedText.toLowerCase())){
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

    private Boolean recipeListFound(){
        Boolean recipeListFound = findElementByID("de.pixelhouse:id/list");
        return recipeListFound;
    }

    public Boolean recipeTitleContains(String titleFragment){
        Boolean textFound = false;
        if(recipeListFound()){
            // Packt alle TextViewsElemente der Vorschläge in eine Liste
            List<MobileElement> recipes = recipeListContainer.findElementsByClassName("android.widget.TextView");
            // Geht alle Rezepte durch und vergleicht sie mit dem Suchbegriff
            // Suchbegriffe werden immer Kleingeschrieben
            for (MobileElement textElement : recipes){
                String recipeText = textElement.getText().toLowerCase();
                if (recipeText.contains(titleFragment.toLowerCase())){
                    textFound = true;
                    System.out.println("Liste enthält: " + titleFragment);
                }
            }
            return textFound;
        } else {
            return textFound;
        }
    }

    /**
     *  Durchsucht die Titel der angezeigten Rezepte nach einem Textfragment
     *  Groß- und Kleinschreibung sind irrelevant. Alle Texte werden in Kleinbuchstaben verglichen.
     * @param titleFragment Textstück, dass in den Rezeptiteln gesucht werden sol
     */
    public void clickRecipe(String titleFragment){
        if(recipeTitleContains(titleFragment)){
            Boolean recipeFound = false;
            List<MobileElement> recipeList = recipeListContainer.findElementsByClassName("android.widget.TextView");
            ListIterator<MobileElement> recipeListIterator = recipeList.listIterator();
            //durchsucht die Titel der Rezepte nach dem Fragment
            while(!recipeFound && recipeListIterator.hasNext()){
                //speichert den nächsten Titel in Kleinbuchstaben in einem String
                String recipeTitle = recipeListIterator.next().getText().toLowerCase();
                //vergleicht den nächsten Titel mit dem gesuchten Fragment
                if (recipeTitle.contains(titleFragment.toLowerCase())){
                    recipeFound = true;
                    recipeListIterator.previous();
                    click(recipeListIterator.next());
                }
            }
        }
    }
}
