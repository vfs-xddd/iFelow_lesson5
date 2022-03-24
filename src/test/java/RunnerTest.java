import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import utils.PropertyManager;

/** Главный настроченый класс Cucumber.
 * <p>Задается базовая конфигурация всех тестов проекта.</p>
 *
 * @author Maksim_Kachaev
 * */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinition", "hooks"},
        plugin = {"pretty", "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm", "json:target/cucumber,json"},
        tags = "@All")
public class RunnerTest {

    /**Действия перед всеми тестами.
     * <p>Инициализация входных данных из проперти, добавление поддержки allure</p>
     * */
    @BeforeClass
    public static void before() {
        initTestProperty();
        addAllure();
    }

    /**Добавляет поддержку отчетов Allure*/
    private static void addAllure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
        );
    }

    /**Подключает входные данные*/
    private static void initTestProperty() {new PropertyManager();}

}
