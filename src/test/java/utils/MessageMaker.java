package utils;

/**Класс для построения различных сообщений.
 *
 * @author Maksim_Kachaev*/
public class MessageMaker {

    /**Содает сообщение следующей структуры, для частного использования.*/
    public static String get1(String temp, String givenUrl, String tagMustBe) {
        String errMsgMask = "==> " +
                "\n==============================================================\n" +
                "?\n==============================================================\n\n";
        return errMsgMask.replace("?", "Для выбранного шаблона сайта \"" + temp +
                "\" с url \"" + givenUrl +
                "\"\nдопустимый тег сценария \"" + tagMustBe + "\"!");
    }
}
