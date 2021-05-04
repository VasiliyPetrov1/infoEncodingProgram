package controller;

import model.MessageInfo;
import model.Model;
import model.dataStructures.ShannonFanoAlgoDataContainer;
import model.dataStructures.SFAlgoTrie;
import service.EncodingDecodingService;
import strategy.decodingAlgoStrategies.ExtendedHammingDecodeAlgoStrategy;
import strategy.decodingAlgoStrategies.HammingDecodeAlgoStrategy;
import strategy.encodingAlgoStrategies.ExtendHammingEncodeAlgoStrategy;
import strategy.encodingAlgoStrategies.HammingEncodeAlgoStrategy;
import utility.convertationUtilities.ConsoleInputProcessingUtility;
import utility.convertationUtilities.FileInputProcessingUtility;
import utility.generalIOUtilities.FileIOUtility;
import view.View;

import java.io.IOException;
import java.util.*;

public class Controller {

    private final View view;
    private Model model;

    public Controller(Model model, View view) {
        this.view = view;
        this.model = model;
    }

    public void processUser() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        View.printMessage(View.GREETING_MESSAGE);
        chooseAction(scanner);
        scanner.close();
    }

    public void chooseAction(Scanner scanner) {
        int responseNum;
        boolean returnFlag = false;
        MessageInfo messageInfo;
        EncodingDecodingService encDecService = model.getEncCodeService();
        View.printMessage(View.ACTION_CHOICE_MESSAGE);
        responseNum = scanner.nextInt();
        while (true) {
            if (responseNum == 0) {
                break;
            } else if (responseNum == 1) {
                responseNum = -1;
                returnFlag = true;
                messageInfo = processMessage(scanner);
                if (messageInfo != null) {
                    System.out.println(messageInfo);
                    //model.getMessageInfoContainer().add(messageInfo);
                }
            } else if (responseNum == 2) {
                responseNum = -1;
                returnFlag = true;
                messageInfo = processMessage(scanner);
                int trieId;
                if (messageInfo != null) {
                    ShannonFanoAlgoDataContainer algoCont = new ShannonFanoAlgoDataContainer(messageInfo);
                    BitSet codeBitSet = algoCont.executeShannonFanoEncoding(); // obtain byte array representation of
                    // the Shannon-Fano code of message
                    SFAlgoTrie trie = algoCont.buildSFAlgoTrie(); // creating decoding tree
                    trieId = (Integer) model.getSFAlgoTrieContainer().add(trie); // save decoding trie and obtain its index
                    System.out.println("avCodeCombLength = " + algoCont.countAvCodeCombLength());
                    System.out.println("avCodeCombLength = " + algoCont.countCompressionRatio());
                    System.out.println("The encoding trie Id is " + trieId);
                    View.printMessage(View.CODE_LOCATION_CHOICE);
                    int codeLocationChoice = scanner.nextInt();
                    if (codeLocationChoice == 1) {
                        byte[] byteCodeArray =
                                algoCont.convertBitSetToByteArray(codeBitSet, algoCont.getSFCodeLength(), trieId);
                        View.printMessage(View.ENTER_CODE_FILE_DESTINATION_REQUEST);
                        String filePath = scanner.nextLine();
                        filePath = scanner.nextLine();
                        try {
                            FileIOUtility.writeBinary(filePath, byteCodeArray);
                        } catch (Exception e) {
                            View.printMessage(e.getMessage());
                        }
                    } else if (codeLocationChoice == 2) {
                        algoCont.printSFCode();
                    }
                }
            } else if (responseNum == 3) {
                responseNum = -1;
                returnFlag = true;
                List resList = new ArrayList();
                View.printMessage(View.CODE_INPUT_TYPE_CHOICE);
                int choiceNum = chooseInputType(scanner);
                if (choiceNum == 1) {
                    View.printMessage(View.SF_CODE_REQUEST);
                    String SFCodeString = scanner.next();
                    View.printMessage(View.DECODING_TRIE_ID_CHOICE);
                    int decodingTrieId = scanner.nextInt();
                    SFAlgoTrie decodingTrie = model.getSFAlgoTrieContainer().get(decodingTrieId);
                    try {
                        resList = ShannonFanoAlgoDataContainer.executeShannonFanoDecoding(SFCodeString, decodingTrie);
                    } catch (Exception e) {
                        View.printError(e.getMessage());
                    }
                    for (Object curElement : resList) {
                        System.out.print(curElement);
                    }
                    System.out.println();

                } else if (choiceNum == 2) {
                    View.printMessage(View.ENTER_CODE_FILE_DESTINATION_REQUEST);
                    String filePath = scanner.nextLine();
                    filePath = scanner.nextLine();
                    byte[] byteCodeArr = null;
                    try{
                        byteCodeArr = FileIOUtility.readBinary(filePath);
                        byte[] resArray =
                                ShannonFanoAlgoDataContainer.executeShannonFanoDecoding(byteCodeArr, model.getSFAlgoTrieContainer());
                        View.printMessage(View.ENTER_RES_FILE_PATH_REQUEST);
                        String resFilePath = scanner.nextLine();
                        //resFilePath = scanner.nextLine();
                        FileIOUtility.writeBinary(resFilePath, resArray);
                    } catch (Exception e) {
                         View.printError(e.getMessage());
                    }

                }
            }else if (responseNum == 4) {
                responseNum = -1;
                returnFlag = true;
                // here we set needed algorithm
                encDecService.setEncodingAlgoStrategy(new HammingEncodeAlgoStrategy());
                encDecService.setDecodingAlgoStrategy(new ExtendedHammingDecodeAlgoStrategy());
                boolean[] hamCode;
                View.printMessage(View.INPUT_TYPE_CHOICE_MESSAGE);
                int inputChoiceNum = chooseInputType(scanner);
                View.printMessage(View.ENTER_HAMMING_CODE_TYPE_REQUEST);
                int codeType = scanner.nextInt();
                boolean codeISExtended = false;
                if (codeType == 2) {
                    codeISExtended = true;
                }
                if (inputChoiceNum == 1) {
                    View.printMessage(View.ENTER_MESSAGE_REQUEST);
                    String inputMessage = scanner.nextLine();
                    inputMessage = scanner.nextLine();
                    hamCode = encDecService.executeDividedEncoding(inputMessage, 16,codeISExtended);
                } else if (inputChoiceNum == 2) {
                    View.printMessage(View.ENTER_FILE_PATH_REQUEST);
                    byte[] byteArr = null;
                    String filePath = scanner.nextLine();
                    filePath = scanner.nextLine();
                    byteArr = FileIOUtility.readBinary(filePath);
                    //byteArr = new byte[]{75, 63};
                    hamCode = encDecService.executeDividedEncoding(byteArr, 8, codeISExtended);
                    System.out.println("curExtCode " + Arrays.toString(hamCode).replaceAll("true", "1").replaceAll("false", "0"));
                    byte[] resByteArr = encDecService.convertBitSetToByteArr(hamCode);
                    System.out.println(Arrays.toString(resByteArr));
                    try {
                        FileIOUtility.writeBinary(filePath, resByteArr);
                    } catch (IOException e) {
                        View.printError(e.getMessage());
                    }
                    //hamCode = encDecService.damageHammingCodeBitSet(hamCode, 8, false, 1);
                    //System.out.println("curExtCode " + Arrays.toString(hamCode).replaceAll("true", "1").replaceAll("false", "0"));
                } else {
                    hamCode = new boolean[]{false};
                }
                /*if (codeType == 2) {
                    encDecService.setEncodingAlgoStrategy(new ExtendHammingEncodeAlgoStrategy());
                    hamCode = encDecService.executeEncoding(hamCode);
                }*/
            }else if (responseNum == 5) {
                responseNum = -1;
                returnFlag = true;
                encDecService.setDecodingAlgoStrategy(new HammingDecodeAlgoStrategy());
                boolean codeIsExtended = false;
                View.printMessage(View.ENTER_FILE_PATH_REQUEST);
                String filePath = scanner.nextLine();
                filePath = scanner.nextLine();
                View.printMessage(View.ENTER_HAMMING_CODE_SEQ_LENGTH_REQUEST);
                int codedSequenceLength = scanner.nextInt();
                View.printMessage(View.ENTER_HAMMING_CODE_TYPE_REQUEST);
                int codeType = scanner.nextInt();
                if (codeType == 2) {
                    codeIsExtended = true;
                }
                if (codeIsExtended) {
                    encDecService.setDecodingAlgoStrategy(new ExtendedHammingDecodeAlgoStrategy());
                }
                byte[] byteArr = FileIOUtility.readBinary(filePath);
                boolean[] hamCode = EncodingDecodingService.retrieveHammingCodeFromFileByteArr(byteArr);
                hamCode = encDecService.executeDividedDecoding(hamCode, codedSequenceLength, codeIsExtended);
                byteArr = encDecService.convertBitSetToAliquotByteArr(hamCode);
                try {
                    FileIOUtility.writeBinary(filePath, byteArr);
                } catch (IOException e) {
                    View.printError(e.getMessage());
                }
            }else if (responseNum == 6){
                responseNum = -1;
                returnFlag = true;
                boolean codeIsExtended = false;
                View.printMessage(View.ENTER_FILE_PATH_REQUEST);
                String filePath = scanner.nextLine();
                filePath = scanner.nextLine();
                View.printMessage(View.ENTER_HAMMING_CODE_SEQ_LENGTH_REQUEST);
                int codedSequenceLength = scanner.nextInt();
                View.printMessage(View.ENTER_HAMMING_CODE_TYPE_REQUEST);
                int codeType = scanner.nextInt();
                if (codeType == 2) {
                    codeIsExtended = true;
                }
                View.printMessage(View.ENTER_HAMMING_CODE_DAM_BITS_NUM_REQUEST);
                int damBitsNum = scanner.nextInt();
                byte[] byteArr = FileIOUtility.readBinary(filePath);
                boolean[] hamCode = EncodingDecodingService.retrieveHammingCodeFromFileByteArr(byteArr);
                int lastByteBitsLength = byteArr[byteArr.length - 1];
                hamCode = encDecService.damageHammingCodeBitSet(hamCode, codedSequenceLength,
                        codeIsExtended, damBitsNum); // here we obtain already damaged Hamming code
                byteArr = encDecService.convertBitSetToByteArr(hamCode);
                try {
                    FileIOUtility.writeBinary(filePath, byteArr);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (returnFlag) {
                responseNum = scanner.nextInt();
            } else {
                View.printError(View.NOT_RIGHT_ACTION_MESSAGE_WARN);
                responseNum = scanner.nextInt();
            }
            View.printMessage(View.ACTION_CHOICE_MESSAGE);
        }

    }

    public MessageInfo processMessage(Scanner scanner) {
        int inputType = chooseInputType(scanner); // 1 - console, 2 - file
        if (inputType == 1) {
            ConsoleInputProcessingUtility inputProcUtil = ConsoleInputProcessingUtility.getInstance();
            View.printMessage(View.ENTER_MESSAGE_REQUEST);
            String input = scanner.nextLine();
            input = scanner.nextLine();
            MessageInfo<Character> messageInfo = inputProcUtil.retrieveInputInfo(input);
            return messageInfo;
        } else {
            FileInputProcessingUtility inputProcUtil = FileInputProcessingUtility.getInstance();
            View.printMessage(View.ENTER_FILE_PATH_REQUEST);
            MessageInfo<Byte> messageInfo;
            String input = scanner.nextLine();
            input = scanner.nextLine();
            try {
                messageInfo = inputProcUtil.retrieveInputInfo(input);
                return messageInfo;
            } catch (IllegalArgumentException e) {
                View.printMessage(e.getMessage());
                return null;
            }
        }
    }

    public int chooseInputType(Scanner scanner) {
        View.printMessage(View.INPUT_TYPE_CHOICE_MESSAGE);
        int inputType = scanner.nextInt();

        while (inputType != 1 && inputType != 2) {
            View.printError(View.NOT_RIGHT_INPUT_CHOICE_WARN);
            inputType = scanner.nextInt();
        }

        return inputType;
    }

}
