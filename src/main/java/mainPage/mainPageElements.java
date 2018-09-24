package mainPage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class mainPageElements {

    @FindBy(id = "searchText")
    public WebElement SEARCHTEXT;

    @FindBy(xpath = "//button[@value='Ara']")
    public WebElement SEARCHBUTTON;

    @FindBy(css = ".results-by-categories > ul > li")
    public List<WebElement> RESULTCATEGORIES;

}
