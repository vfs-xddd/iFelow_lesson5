package hooks;

import utils.PropertyManager;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;

/** Настроечный класс для всех тестов.
 *
 * @author Maksim_Kachaev
 * @see org.junit.jupiter.api.BeforeAll
 * @see org.junit.jupiter.api.AfterAll
 * */
public class TestConfig {

    /**Создает тестовое окружение*/
    @BeforeAll
    public static void initTestEnvironment(){
        initTestProperty();
        addAllure();
    }

    /**Подключает входные данные*/
    private static void initTestProperty() {new PropertyManager();}

    /**Добавляет поддержку отчетов Allure*/ // TODO: 23.03.2022 check this seems dont need
    private static void addAllure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
        );}
}
