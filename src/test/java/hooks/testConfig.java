package hooks;

import Utils.PropertyManager;
import org.junit.jupiter.api.BeforeAll;

public class testConfig {

    @BeforeAll
    private static void init_TestProperty() {
        new PropertyManager();
    }
}
