package hooks;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

/**Класс хуков для cucumber.
 * <p>Содержит преднастроечные действия перед/после каждого сценария, также прочие методы для работы в среде Cucumber</p>
 *
 * @author Maksim_Kachaev*/
public class CucumberHooks {

    /**ScenarioTagsReader
     * <p>Определяет все теги текущего сценария</p>
     * @param scenario захватывается объект текущего сценария
     * @return строка-список тегов сценария
     * @see Before
     * @see System#setProperty
     * */
    @Before(value="@All")
    @CanIgnoreReturnValue
    public String before(Scenario scenario) {
        String tags = scenario.getSourceTagNames().toString();
        Assertions.assertFalse(tags.isEmpty(), "Для чтения тегов сценариев они должны быть указаны!");
        System.setProperty("currentScenarioTags", tags);
        return tags;
    }

    /**Вывод результата для аллюр-отчета
     * @param report строка для вывода в отчете.*/
    @Step("Результат:")
    public static void result(String report){}

}


