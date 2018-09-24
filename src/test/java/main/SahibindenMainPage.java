package main;

import adPage.AdPageElements;
import mainPage.MainPageElements;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.gen.Core;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class SahibindenMainPage {

    private static WebDriver driver;
    private static String HOST = "localhost";
    private static int PROXY = 8090;
    static String BaseURL = "https://www.sahibinden.com/";


    @Before
    public void init() throws IOException, InterruptedException {

        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(HOST + ":" + PROXY)
                .setFtpProxy(HOST + ":" + PROXY)
                .setSslProxy(HOST + ":" + PROXY);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, proxy);

        if (Platform.getCurrent().is(Platform.MAC)) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
        } else if (Platform.getCurrent().is(Platform.WIN10)) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
        }

        Desktop.getDesktop().open(new File("/Applications/OWASP ZAP.app"));
        Thread.sleep(20000);

        driver = new ChromeDriver(cap);
        driver.manage().window().maximize();

    }

    @Test
    public void breadCrumbItemCheck() {

        WebDriverWait wait = new WebDriverWait(driver, 20);

        driver.navigate().to(BaseURL);

        MainPageElements mainpage = PageFactory.initElements(driver, MainPageElements.class);

        wait.until(ExpectedConditions.visibilityOf(mainpage.SEARCHTEXT));
        mainpage.SEARCHTEXT.sendKeys("istanbul");
        mainpage.SEARCHBUTTON.click();

        List<WebElement> resultCategory = mainpage.RESULTCATEGORIES;

        List<String> categoryDataId = new ArrayList<>();

        // Get Category Data ID
        resultCategory.forEach(item -> categoryDataId.add(item.getAttribute("data-id")));

        // Category assert href
        List<String> breadcrumbItem = new ArrayList<String>();
        breadcrumbItem.add("emlak/istanbul");
        breadcrumbItem.add("kategori/vasita");
        breadcrumbItem.add("kategori/yedek-parca-aksesuar-donanim-tuning");
        breadcrumbItem.add("kategori/ikinci-el-ve-sifir-alisveris");
        breadcrumbItem.add("kategori/is-makineleri-sanayi");
        breadcrumbItem.add("hizmetler/");
        breadcrumbItem.add("ozel-ders-verenler");
        breadcrumbItem.add("kategori/is-ilanlari");
        breadcrumbItem.add("yardimci-arayanlar");
        breadcrumbItem.add("kategori/hayvanlar-alemi");


        // Click category and assert href
        IntStream.range(0, categoryDataId.size())
                .forEach(i -> {
                    AdPageElements adPage = PageFactory.initElements(driver, AdPageElements.class);
                    By category = By.cssSelector("[data-id='" + categoryDataId.get(i) + "']");
                    wait.until(ExpectedConditions.elementToBeClickable(category));
                    driver.findElement(category).click();
                    Assert.assertEquals(BaseURL + breadcrumbItem.get(i), adPage.BREADCRUMB.getAttribute("href"));
                    driver.navigate().back();
                });

    }

    @After
    public void teardown() {

        // ZAP api generate HTML Report
        try {
            ClientApi clientApi = new ClientApi(HOST, PROXY);
            Core core = new Core(clientApi);
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/HtmlReport/SahibindenZAPHtmlReport.html");
            fos.write(core.htmlreport("ed06mf5o7nehlpgcbplji3nr68"));
            fos.close();
        } catch (Exception ex) {
        }


        if (driver != null) {
            driver.quit();
        }

    }


}
