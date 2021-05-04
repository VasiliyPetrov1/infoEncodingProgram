package utility.convertationUtilities;

import model.MessageInfo;

import java.util.ArrayList;
import java.util.List;

public class ConsoleInputProcessingUtility implements InputProcessingUtility<Character, String> {

    private static ConsoleInputProcessingUtility consoleUtility;

    private ConsoleInputProcessingUtility() {

    }

    public static ConsoleInputProcessingUtility getInstance() {
        if (consoleUtility == null) {
            consoleUtility = new ConsoleInputProcessingUtility();
        }
        return consoleUtility;
    }

    @Override
    public MessageInfo<Character> retrieveInputInfo(String... input) {

        MessageInfo<Character> messageInfo;
        List<Character> inputCharList = new ArrayList<>();

        for (String curInput : input) {
            for (Character curChar: curInput.toCharArray() ) {
                inputCharList.add(curChar);
            }
        }

        messageInfo = new MessageInfo<>(inputCharList);
        return messageInfo;
    }
}
