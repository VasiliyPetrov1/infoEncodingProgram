package view;

import utility.PropertyFileUtility;

public class View {

    public static final String GREETING_MESSAGE;
    public static final String INPUT_TYPE_CHOICE_MESSAGE;
    public static final String NOT_RIGHT_INPUT_CHOICE_WARN;
    public static final String NOT_RIGHT_ACTION_MESSAGE_WARN;
    public static final String ACTION_CHOICE_MESSAGE;
    public static final String ENTER_MESSAGE_REQUEST;
    public static final String ENTER_FILE_PATH_REQUEST;
    public static final String CODE_LOCATION_CHOICE;
    public static final String ENTER_CODE_FILE_DESTINATION_REQUEST;
    public static final String CODE_INPUT_TYPE_CHOICE;
    public static final String DECODING_TRIE_ID_CHOICE;
    public static final String SF_CODE_REQUEST;
    public static final String ENTER_RES_FILE_PATH_REQUEST;
    public static final String ENTER_HAMMING_CODE_TYPE_REQUEST;
    public static final String ENTER_HAMMING_CODE_SEQ_LENGTH_REQUEST;
    public static final String ENTER_HAMMING_CODE_DAM_BITS_NUM_REQUEST;

    static {
        GREETING_MESSAGE = (String) PropertyFileUtility.getPropertyFromFile("greetingMessage");
        INPUT_TYPE_CHOICE_MESSAGE = (String)
                PropertyFileUtility.getPropertyFromFile("inputTypeChoiceMessage");
        NOT_RIGHT_INPUT_CHOICE_WARN = (String)
                PropertyFileUtility.getPropertyFromFile("notRightInputChoiceWarn");
        NOT_RIGHT_ACTION_MESSAGE_WARN = (String)
                PropertyFileUtility.getPropertyFromFile("notRightActionMessageWarn");
        ACTION_CHOICE_MESSAGE = (String)
                PropertyFileUtility.getPropertyFromFile("actionChoiceMessage");
        ENTER_MESSAGE_REQUEST = (String)
            PropertyFileUtility.getPropertyFromFile("enterMessageRequest");
        ENTER_FILE_PATH_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("enterFilePathRequest");
        CODE_LOCATION_CHOICE = (String)
                PropertyFileUtility.getPropertyFromFile("codeLocationChoice");
        ENTER_CODE_FILE_DESTINATION_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("enterCodeFileDestinationRequest");
        CODE_INPUT_TYPE_CHOICE = (String)
                PropertyFileUtility.getPropertyFromFile("codeInputTypeChoice");
        DECODING_TRIE_ID_CHOICE = (String)
                PropertyFileUtility.getPropertyFromFile("decodingTrieIdChoice");
        SF_CODE_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("SFCodeRequest");
        ENTER_RES_FILE_PATH_REQUEST = (String)
        PropertyFileUtility.getPropertyFromFile("enterResFilePathRequest");
        ENTER_HAMMING_CODE_TYPE_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("enterHammingCodeTypeRequest");
        ENTER_HAMMING_CODE_SEQ_LENGTH_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("enterHammingCodeSeqLengthRequest");
        ENTER_HAMMING_CODE_DAM_BITS_NUM_REQUEST = (String)
                PropertyFileUtility.getPropertyFromFile("enterHammingCodeDamBitsNumRequest");
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.err.println(message);
    }

}
