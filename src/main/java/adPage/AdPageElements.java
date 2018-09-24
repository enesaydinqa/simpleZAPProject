package adPage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class AdPageElements {

    @FindAll({
            @FindBy(xpath = "(//a[@class='sprite breadcrumbItem'])[last()]"),
            @FindBy(css = ".homepage-label")
    })
    public WebElement BREADCRUMB;


}
