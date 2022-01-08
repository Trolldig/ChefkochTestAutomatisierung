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
    private MobileElement navigateBackBtn;

    //Back-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/left_action")
    private MobileElement searchBackBtn;

    //Suchleiste
    @FindBy(id = "de.pixelhouse:id/search_bar_text")
    private MobileElement searchTextField;

    //Kebap Menü
    @FindBy(id = "de.pixelhouse:id/search_bar_overflow_menu")
    private MobileElement kebapMenu;

    //Löschen-Button der Suchleiste
    @FindBy(id = "de.pixelhouse:id/clear_btn")
    private MobileElement searchClearBtn;

    //Suche-Button
    @AndroidFindBy(accessibility = "Suche")
    private MobileElement searchBtn;

    //Container mit Liste der Suchvorschläge
    @AndroidFindBy(id = "de.pixelhouse:id/suggestions_list")
    private MobileElement suggestionContainer;

    //Filter-Btn
    @FindBy(id = "de.pixelhouse:id/search_filter_text_tv")
    private MobileElement filterBtn;

    //4Sterne & mehr Filter-Btn
    @FindBy(id = "de.pixelhouse:id/mr_four_stars")
    private MobileElement filterFourStarsBtn;

    //enthält Filter zurücksetzen-[index 0] und apply-button [2] + anzahl der Suchergebnisse [1]
    @FindBy(id = "de.pixelhouse:id/search_filter_action_panel")
    private MobileElement filterFooter;

    //Rezeptliste
    @FindBy(id = "de.pixelhouse:id/list")
    private MobileElement recipeListContainer;

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

    public void clickFilterApplyBtn(){
        List<MobileElement> filterFooterBtns = filterFooter.findElementsByClassName("android.widget.ImageView");
        MobileElement applyBtn = filterFooterBtns.get(1);
        click(applyBtn);
    }

    /**
     *  Checkt, ob Suchvorschläge vorhanden sind
     * @return true wenn Suchvorschläge vohanden sind, false sonst
     */
    private boolean suggestionContainerFound(){
        boolean suggestionContainerFound = findElementByID("de.pixelhouse:id/suggestions_list");
        return suggestionContainerFound;
    }

    /**
     * Prüft, ob ein bestimmtes Wort in den Suchvorschlägen vorkommt.
     * @param searchedText der gesuchte Begriff
     * @return true wenn das Wort in den Suggestions vorkommt, false sonst
     */
    public boolean suggestionListContainsWord(String searchedText){
        if (suggestionContainerFound()) {
            boolean textFound = false;
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
            boolean suggestionFound = false;
            List<MobileElement> suggestionList = suggestionContainer.findElementsByClassName("android.widget.TextView");
            ListIterator<MobileElement> suggestionListIterator = suggestionList.listIterator();
            while(!suggestionFound && suggestionListIterator.hasNext()){
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
            boolean textFound = false;
            // Packt alle TextViewsElemente der Vorschläge in eine Liste
            List<MobileElement> suggestionList = suggestionContainer.findElementsByClassName("android.widget.TextView");
            Random rand = new Random();
            // Wählt einen zufälligen Begriff aus
            click(suggestionList.get(rand.nextInt(suggestionList.size())));
        } else {
            System.out.println("Keine Vorschläge vorhanden");
        }
    }

    private boolean recipeListFound(){
        boolean recipeListFound = findElementByID("de.pixelhouse:id/list");
        return recipeListFound;
    }

    public boolean recipeTitlesContain(String titleFragment){
        boolean textFound = false;
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
        if(recipeTitlesContain(titleFragment)){
            boolean recipeFound = false;
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

    /**
     *  Tappt auf ein zufälliges Rezept
     */
    public void clickRandomRecipe(){
        if (recipeListFound()) {
            // Packt alle sichtbaren Rezepte in eine Liste
            List<MobileElement> recipeList = recipeListContainer.findElementsByClassName("android.widget.TextView");
            Random rand = new Random();
            // Wählt ein zufälliges Rezept aus
            click(recipeList.get(rand.nextInt(recipeList.size())));
        } else {
            System.out.println("Keine Rezepte vorhanden");
        }
    }
}
