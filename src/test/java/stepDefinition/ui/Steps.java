package stepDefinition.ui;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.То;
import org.junit.jupiter.api.DisplayName;

import java.text.DecimalFormat;
import java.util.Locale;


public class Steps {
    int a;
    int b;
    int sum;
    int diff;
    int mult;
    double div;

    @Дано("^два числа (.*) и (.*)$")
    public void inputs(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @То("находим сумму двух чисел")
    public void summa() {this.sum = a + b;}

    @То("находим разность двух чисел")
    public void difference() {this.diff = a - b;}

    @То("находим произведение двух чисел")
    public void multiplise() {this.mult = a * b;}

    @То("находим частное двух чисел")
    public void divine() {

        this.div = (double) a / b;}

    @DisplayName("вводим \"разность\" или \"сумму\"")
    @Затем("^выводим ([^\"]*) на экран$")
    public void printResult(String input) {
        double res = 0;
        switch (input.toLowerCase(Locale.ROOT)) {
            case "сумму":           res = sum; break;
            case "разность":        res = diff; break;
            case "произведение":    res = mult; break;
            case "частное":         res = div; break;
        }
        String res_formated = String.format(String.valueOf(res), new DecimalFormat("#0.00"));
        System.out.println(input + " чисел " + a + " и " + b + " равняется: " + res_formated);
    }

}
